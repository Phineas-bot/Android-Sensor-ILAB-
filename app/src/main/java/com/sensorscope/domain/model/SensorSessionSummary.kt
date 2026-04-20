package com.sensorscope.domain.model

data class SensorSessionSummary(
    val id: Long,
    val name: String,
    val startedAtMillis: Long,
    val endedAtMillis: Long?,
    val readingCount: Int = 0
) {
    /** Duration in seconds, or null if the session is still in progress. */
    val durationSeconds: Long?
        get() = endedAtMillis?.let { (it - startedAtMillis) / 1000L }
}
