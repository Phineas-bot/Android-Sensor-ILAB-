package com.sensorscope.domain.usecase

import com.sensorscope.core.model.SensorData
import com.sensorscope.core.model.SensorType
import com.sensorscope.domain.repository.SensorRepository
import javax.inject.Inject

class ManageSensorSessionUseCase @Inject constructor(
    private val repository: SensorRepository
) {
    suspend fun startSession(name: String): Long = repository.startSession(name)

    suspend fun stopSession(sessionId: Long) = repository.stopSession(sessionId)

    suspend fun log(sessionId: Long, type: SensorType, data: SensorData) {
        repository.logReading(sessionId, type, data)
    }

    fun availableSensors(): Map<SensorType, Boolean> = repository.availableSensors()
}
