package com.sensorscope.presentation.sensor

import com.sensorscope.core.model.SensorData
import com.sensorscope.core.model.SamplingMode
import com.sensorscope.core.model.SensorType
import com.sensorscope.domain.analytics.SensorAnalyticsEngine
import com.sensorscope.domain.usecase.ManageSensorSessionUseCase
import com.sensorscope.domain.usecase.ObserveSensorDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SensorViewModelScopeTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun updatesOnlyActiveScopeSensorDataWhenInDetailScope() = runTest {
        val flows = mapOf(
            SensorType.ACCELEROMETER to MutableSharedFlow<SensorData>(extraBufferCapacity = 1),
            SensorType.GYROSCOPE to MutableSharedFlow<SensorData>(extraBufferCapacity = 1),
            SensorType.MAGNETOMETER to MutableSharedFlow<SensorData>(extraBufferCapacity = 1)
        )

        val observeUseCase = ObserveSensorDataUseCase(
            repository = object : com.sensorscope.domain.repository.SensorRepository {
                override fun availableSensors(): Map<SensorType, Boolean> = SensorType.entries.associateWith { true }
                override fun observeSensor(type: SensorType, samplingMode: SamplingMode): Flow<SensorData> =
                    flows.getValue(type)
                override suspend fun startSession(name: String): Long = 1L
                override suspend fun stopSession(sessionId: Long) = Unit
                override suspend fun logReading(sessionId: Long, sensorType: SensorType, data: SensorData) = Unit
                override fun observeSessions() = kotlinx.coroutines.flow.emptyFlow<List<com.sensorscope.domain.model.SensorSessionSummary>>()
                override suspend fun getReadingsForSession(sessionId: Long) = emptyList<com.sensorscope.domain.repository.LoggedReading>()
            }
        )

        val manageUseCase = ManageSensorSessionUseCase(
            repository = object : com.sensorscope.domain.repository.SensorRepository {
                override fun availableSensors(): Map<SensorType, Boolean> = SensorType.entries.associateWith { true }
                override fun observeSensor(type: SensorType, samplingMode: SamplingMode): Flow<SensorData> =
                    flows.getValue(type)
                override suspend fun startSession(name: String): Long = 1L
                override suspend fun stopSession(sessionId: Long) = Unit
                override suspend fun logReading(sessionId: Long, sensorType: SensorType, data: SensorData) = Unit
                override fun observeSessions() = kotlinx.coroutines.flow.emptyFlow<List<com.sensorscope.domain.model.SensorSessionSummary>>()
                override suspend fun getReadingsForSession(sessionId: Long) = emptyList<com.sensorscope.domain.repository.LoggedReading>()
            }
        )

        val viewModel = SensorViewModel(observeUseCase, manageUseCase, SensorAnalyticsEngine())
        viewModel.setCollectionScope(SensorCollectionScope.Detail(SensorType.ACCELEROMETER))
        viewModel.startListening()

        flows.getValue(SensorType.ACCELEROMETER).tryEmit(SensorData(4f, 5f, 6f, 10L))
        flows.getValue(SensorType.GYROSCOPE).tryEmit(SensorData(1f, 2f, 3f, 11L))

        val state = viewModel.uiState.first { it.latestValues.containsKey(SensorType.ACCELEROMETER) }
        assertTrue(state.latestValues.containsKey(SensorType.ACCELEROMETER))
        assertFalse(state.latestValues.containsKey(SensorType.GYROSCOPE))
    }
}
