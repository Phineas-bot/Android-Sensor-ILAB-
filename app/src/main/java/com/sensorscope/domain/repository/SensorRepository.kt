package com.sensorscope.domain.repository

import com.sensorscope.core.model.SensorData
import com.sensorscope.core.model.SensorType
import com.sensorscope.domain.model.SensorSessionSummary
import kotlinx.coroutines.flow.Flow

interface SensorRepository {
    fun availableSensors(): Map<SensorType, Boolean>
    fun observeSensor(type: SensorType): Flow<SensorData>
    suspend fun startSession(name: String): Long
    suspend fun stopSession(sessionId: Long)
    suspend fun logReading(sessionId: Long, sensorType: SensorType, data: SensorData)
    fun observeSessions(): Flow<List<SensorSessionSummary>>
    suspend fun getReadingsForSession(sessionId: Long): List<LoggedReading>
}

data class LoggedReading(
    val sensorType: SensorType,
    val x: Float,
    val y: Float,
    val z: Float,
    val timestampNanos: Long,
    val recordedAtMillis: Long
)
