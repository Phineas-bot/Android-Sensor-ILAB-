package com.sensorscope.presentation.sensor

import com.sensorscope.core.model.SensorType

sealed class SensorCollectionScope {
    /** Dashboard: the required set is determined externally (by enabledSensors in the VM). */
    data object Dashboard : SensorCollectionScope()
    /** Detail: always collect one specific sensor. */
    data class Detail(val sensorType: SensorType) : SensorCollectionScope()
    /** Labs: always collect the full set needed by all lab experiments. */
    data object Labs : SensorCollectionScope()

    companion object {
        /** All sensors required by the Labs experiments. */
        val LAB_SENSORS = setOf(
            SensorType.ACCELEROMETER,
            SensorType.MAGNETOMETER,
            SensorType.GYROSCOPE,
            SensorType.LIGHT
        )
    }
}
