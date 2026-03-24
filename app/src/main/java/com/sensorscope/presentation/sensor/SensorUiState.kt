package com.sensorscope.presentation.sensor

import com.sensorscope.core.model.SensorData
import com.sensorscope.core.model.SensorType

data class SensorUiState(
    val isListening: Boolean = false,
    val availableSensors: Map<SensorType, Boolean> = emptyMap(),
    val latestValues: Map<SensorType, SensorData> = emptyMap(),
    val chartSeries: Map<SensorType, List<SensorData>> = emptyMap(),
    val currentSessionId: Long? = null,
    val errorMessage: String? = null
)
