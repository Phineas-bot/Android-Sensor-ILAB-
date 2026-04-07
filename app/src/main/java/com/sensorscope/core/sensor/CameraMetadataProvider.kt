package com.sensorscope.core.sensor

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.core.content.ContextCompat
import com.sensorscope.core.model.SamplingMode
import com.sensorscope.core.model.SensorData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

@Singleton
class CameraMetadataProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun isAvailable(): Boolean {
        val manager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager ?: return false
        return try {
            hasCameraPermission() && manager.cameraIdList.isNotEmpty()
        } catch (_: Exception) {
            false
        }
    }

    fun observe(samplingMode: SamplingMode): Flow<SensorData> = callbackFlow {
        val manager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
        if (manager == null || !isAvailable()) {
            close()
            return@callbackFlow
        }

        val refreshMs = when (samplingMode) {
            SamplingMode.NORMAL -> 1500L
            SamplingMode.FAST -> 700L
        }

        val job: Job = launch(Dispatchers.Default) {
            val cameraId = selectCameraId(manager) ?: return@launch
            while (true) {
                val score = readMetadataScore(manager, cameraId)
                trySend(
                    SensorData(
                        x = score,
                        y = 0f,
                        z = 0f,
                        timestamp = System.nanoTime()
                    )
                )
                delay(refreshMs)
            }
        }

        awaitClose { job.cancel() }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
    }

    private fun selectCameraId(manager: CameraManager): String? {
        val ids = manager.cameraIdList
        if (ids.isEmpty()) return null
        ids.forEach { id ->
            val characteristics = manager.getCameraCharacteristics(id)
            val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
            if (facing == CameraCharacteristics.LENS_FACING_BACK) {
                return id
            }
        }
        return ids.firstOrNull()
    }

    private fun readMetadataScore(manager: CameraManager, cameraId: String): Float {
        return try {
            val c = manager.getCameraCharacteristics(cameraId)
            val maxZoom = c.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM) ?: 1f
            val apertures = c.get(CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES)
            val focalLengths = c.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)

            val aperture = apertures?.average()?.toFloat() ?: 2.0f
            val focal = focalLengths?.average()?.toFloat() ?: 3.5f

            // A compact normalized metadata score for trend/correlation analytics.
            ((maxZoom * 10f) + (focal * 5f) + (10f / aperture)).coerceIn(0f, 100f)
        } catch (_: Exception) {
            0f
        }
    }
}
