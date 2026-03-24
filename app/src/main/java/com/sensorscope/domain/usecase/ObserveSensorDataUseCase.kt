package com.sensorscope.domain.usecase

import com.sensorscope.core.model.SensorData
import com.sensorscope.core.model.SensorType
import com.sensorscope.domain.repository.SensorRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveSensorDataUseCase @Inject constructor(
    private val repository: SensorRepository
) {
    operator fun invoke(type: SensorType): Flow<SensorData> = repository.observeSensor(type)
}
