package com.sensorscope.data.repository

import com.sensorscope.core.model.SensorData
import com.sensorscope.core.model.SensorType
import com.sensorscope.core.model.SamplingMode
import com.sensorscope.core.sensor.SensorManagerHelper
import com.sensorscope.data.local.dao.SensorReadingDao
import com.sensorscope.data.local.dao.SensorSessionDao
import com.sensorscope.data.local.entity.SensorReadingEntity
import com.sensorscope.data.local.entity.SensorSessionEntity
import com.sensorscope.domain.model.SensorSessionSummary
import com.sensorscope.domain.repository.LoggedReading
import com.sensorscope.domain.repository.SensorRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.sample

@Singleton
class SensorRepositoryImpl @Inject constructor(
    private val sensorManagerHelper: SensorManagerHelper,
    private val sessionDao: SensorSessionDao,
    private val readingDao: SensorReadingDao
) : SensorRepository {

    override fun availableSensors(): Map<SensorType, Boolean> = sensorManagerHelper.availableSensors()

    @OptIn(FlowPreview::class)
    override fun observeSensor(type: SensorType, samplingMode: SamplingMode): Flow<SensorData> {
        return sensorManagerHelper.observeSensor(type, samplingMode)
            .sample(samplingMode.toSampleWindow())
            .flowOn(Dispatchers.Default)
    }

    private fun SamplingMode.toSampleWindow() =
        when (this) {
            SamplingMode.NORMAL -> 100.milliseconds
            SamplingMode.FAST -> 33.milliseconds
        }

    override suspend fun startSession(name: String): Long {
        return sessionDao.insertSession(
            SensorSessionEntity(
                name = name,
                startedAtMillis = System.currentTimeMillis()
            )
        )
    }

    override suspend fun stopSession(sessionId: Long) {
        sessionDao.closeSession(sessionId, System.currentTimeMillis())
    }

    override suspend fun logReading(sessionId: Long, sensorType: SensorType, data: SensorData) {
        readingDao.insertReading(
            SensorReadingEntity(
                sessionId = sessionId,
                sensorType = sensorType.name,
                x = data.x,
                y = data.y,
                z = data.z,
                timestampNanos = data.timestamp,
                recordedAtMillis = System.currentTimeMillis()
            )
        )
    }

    override fun observeSessions(): Flow<List<SensorSessionSummary>> {
        return sessionDao.observeSessions().map { sessions ->
            sessions.map {
                SensorSessionSummary(
                    id = it.id,
                    name = it.name,
                    startedAtMillis = it.startedAtMillis,
                    endedAtMillis = it.endedAtMillis
                )
            }
        }
    }

    override suspend fun getReadingsForSession(sessionId: Long): List<LoggedReading> {
        return readingDao.getReadingsBySession(sessionId).mapNotNull { reading ->
            val type = runCatching { SensorType.valueOf(reading.sensorType) }.getOrNull() ?: return@mapNotNull null
            LoggedReading(
                sensorType = type,
                x = reading.x,
                y = reading.y,
                z = reading.z,
                timestampNanos = reading.timestampNanos,
                recordedAtMillis = reading.recordedAtMillis
            )
        }
    }
}
