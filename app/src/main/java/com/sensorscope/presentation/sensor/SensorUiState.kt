package com.sensorscope.presentation.sensor

import com.sensorscope.core.model.SensorData
import com.sensorscope.core.model.SensorType
import com.sensorscope.core.model.SamplingMode
import com.sensorscope.domain.analytics.SensorTrendSummary

data class SensorUiState(
    /** True only when a DB session is actively recording. */
    val isRecording: Boolean = false,
    /** Which sensors the user has individually toggled ON on the Dashboard. */
    val enabledSensors: Set<SensorType> = emptySet(),
    val samplingMode: SamplingMode = SamplingMode.NORMAL,
    val availableSensors: Map<SensorType, Boolean> = emptyMap(),
    val latestValues: Map<SensorType, SensorData> = emptyMap(),
    val chartSeries: Map<SensorType, List<SensorData>> = emptyMap(),
    val trendSummaries: Map<SensorType, SensorTrendSummary> = emptyMap(),
    val crossSensorInsights: List<String> = emptyList(),
    val currentSessionId: Long? = null,
    val errorMessage: String? = null
) {
    /** Backwards-compat alias used by Detail / Labs screens. */
    val isListening: Boolean get() = isRecording
}
