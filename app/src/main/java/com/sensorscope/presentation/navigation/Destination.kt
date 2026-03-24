package com.sensorscope.presentation.navigation

import com.sensorscope.core.model.SensorType

sealed class Destination(val route: String) {
    data object Dashboard : Destination("dashboard")
    data object Labs : Destination("labs")
    data object Logs : Destination("logs")
    data object SensorDetail : Destination("sensor_detail/{sensorType}") {
        fun createRoute(sensorType: SensorType): String = "sensor_detail/${sensorType.name}"
    }
}
