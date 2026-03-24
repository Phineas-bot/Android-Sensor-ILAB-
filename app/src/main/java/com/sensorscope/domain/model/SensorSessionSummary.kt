package com.sensorscope.domain.model

data class SensorSessionSummary(
    val id: Long,
    val name: String,
    val startedAtMillis: Long,
    val endedAtMillis: Long?
)
