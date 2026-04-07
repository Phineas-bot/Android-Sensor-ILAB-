package com.sensorscope.presentation.sensor

import com.sensorscope.core.model.SensorType

sealed class SensorCollectionScope {
    data object Dashboard : SensorCollectionScope()
    data class Detail(val sensorType: SensorType) : SensorCollectionScope()
    data object Labs : SensorCollectionScope()

    fun requiredSensors(): Set<SensorType> {
        return when (this) {
            Dashboard -> SensorType.entries.toSet()
            is Detail -> setOf(sensorType)
            Labs -> setOf(
                SensorType.ACCELEROMETER,
                SensorType.MAGNETOMETER,
                SensorType.GYROSCOPE,
                SensorType.LIGHT,
                SensorType.PROXIMITY,
                SensorType.PRESSURE,
                SensorType.AUDIO_LEVEL,
                SensorType.BLUETOOTH_SIGNAL,
                SensorType.CAMERA_METADATA
            )
        }
    }
}
