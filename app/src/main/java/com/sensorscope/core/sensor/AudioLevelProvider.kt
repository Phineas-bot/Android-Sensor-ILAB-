package com.sensorscope.core.sensor

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.content.ContextCompat
import com.sensorscope.core.model.SamplingMode
import com.sensorscope.core.model.SensorData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.log10
import kotlin.math.sqrt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

@Singleton
class AudioLevelProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun isAvailable(): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
            PackageManager.PERMISSION_GRANTED
    }

    fun observe(samplingMode: SamplingMode): Flow<SensorData> = callbackFlow {
        if (!isAvailable()) {
            close()
            return@callbackFlow
        }

        val sampleRate = 8000
        val channelConfig = AudioFormat.CHANNEL_IN_MONO
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT
        val minBuffer = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
        if (minBuffer <= 0) {
            close()
            return@callbackFlow
        }

        val bufferSize = minBuffer * 2
        val record = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize
        )

        if (record.state != AudioRecord.STATE_INITIALIZED) {
            record.release()
            close()
            return@callbackFlow
        }

        val frameDelayMs = when (samplingMode) {
            SamplingMode.NORMAL -> 180L
            SamplingMode.FAST -> 70L
        }

        record.startRecording()
        val readBuffer = ShortArray(bufferSize / 2)

        val job: Job = launch(Dispatchers.IO) {
            while (true) {
                val read = record.read(readBuffer, 0, readBuffer.size)
                if (read > 0) {
                    val db = computeDecibel(readBuffer, read)
                    trySend(
                        SensorData(
                            x = db,
                            y = 0f,
                            z = 0f,
                            timestamp = System.nanoTime()
                        )
                    )
                }
                delay(frameDelayMs)
            }
        }

        awaitClose {
            job.cancel()
            try {
                record.stop()
            } catch (_: IllegalStateException) {
            }
            record.release()
        }
    }

    private fun computeDecibel(samples: ShortArray, count: Int): Float {
        var sum = 0.0
        for (i in 0 until count) {
            val sample = samples[i].toDouble()
            sum += sample * sample
        }
        val rms = sqrt(sum / count.coerceAtLeast(1))
        if (rms <= 0.0) return 0f
        return (20.0 * log10(rms / 32767.0 + 1e-9)).toFloat().coerceIn(-90f, 0f)
    }
}
