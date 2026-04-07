package com.sensorscope.core.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.sensorscope.core.model.SensorData
import com.sensorscope.core.model.SensorSample
import com.sensorscope.core.model.SensorType
import com.sensorscope.core.model.SamplingMode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@Singleton
class SensorManagerHelper @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val sensors: Map<SensorType, Sensor?> = mapOf(
        SensorType.ACCELEROMETER to sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
        SensorType.GYROSCOPE to sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
        SensorType.MAGNETOMETER to sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
        SensorType.LIGHT to sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
        SensorType.PROXIMITY to sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
        SensorType.PRESSURE to sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
    )

    fun availableSensors(): Map<SensorType, Boolean> = sensors.mapValues { it.value != null }

    fun observeSensor(sensorType: SensorType, samplingMode: SamplingMode): Flow<SensorData> = callbackFlow {
        val sensor = sensors[sensorType]
        if (sensor == null) {
            close()
            return@callbackFlow
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                val values = event?.values ?: return
                if (values.isEmpty()) return

                trySend(
                    SensorData(
                        x = values[0],
                        y = values.getOrElse(1) { 0f },
                        z = values.getOrElse(2) { 0f },
                        timestamp = event.timestamp
                    )
                )
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        sensorManager.registerListener(listener, sensor, samplingMode.toSensorDelay())

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }

    private fun SamplingMode.toSensorDelay(): Int {
        return when (this) {
            SamplingMode.NORMAL -> SensorManager.SENSOR_DELAY_NORMAL
            SamplingMode.FAST -> SensorManager.SENSOR_DELAY_GAME
        }
    }

    fun observeAllSensors(): Flow<SensorSample> = callbackFlow {
        val listeners = mutableListOf<SensorEventListener>()

        sensors.forEach { (type, sensor) ->
            if (sensor == null) return@forEach

            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    val values = event?.values ?: return
                    if (values.isEmpty()) return
                    trySend(
                        SensorSample(
                            type = type,
                            data = SensorData(
                                x = values[0],
                                y = values.getOrElse(1) { 0f },
                                z = values.getOrElse(2) { 0f },
                                timestamp = event.timestamp
                            )
                        )
                    )
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
            }

            listeners += listener
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        awaitClose {
            listeners.forEach(sensorManager::unregisterListener)
        }
    }
}
