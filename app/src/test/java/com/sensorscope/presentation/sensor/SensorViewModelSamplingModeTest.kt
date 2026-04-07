package com.sensorscope.presentation.sensor

import com.sensorscope.core.model.SamplingMode
import com.sensorscope.core.model.SensorData
import com.sensorscope.core.model.SensorType
import com.sensorscope.domain.model.SensorSessionSummary
import com.sensorscope.domain.repository.LoggedReading
import com.sensorscope.domain.repository.SensorRepository
import com.sensorscope.domain.usecase.ManageSensorSessionUseCase
import com.sensorscope.domain.usecase.ObserveSensorDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SensorViewModelSamplingModeTest {

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
    fun switchingToFastSamplingRestartsCollectorsWithFastMode() = runTest {
        val repository = FakeSensorRepository()
        val viewModel = SensorViewModel(
            observeSensorDataUseCase = ObserveSensorDataUseCase(repository),
            manageSensorSessionUseCase = ManageSensorSessionUseCase(repository)
        )

        viewModel.setCollectionScope(SensorCollectionScope.Detail(SensorType.ACCELEROMETER))
        viewModel.startListening()
        advanceUntilIdle()

        assertTrue(repository.observeRequests.contains(SensorType.ACCELEROMETER to SamplingMode.NORMAL))

        viewModel.setSamplingRateFast(true)
        advanceUntilIdle()

        assertEquals(SamplingMode.FAST, viewModel.uiState.value.samplingMode)
        assertTrue(repository.observeRequests.contains(SensorType.ACCELEROMETER to SamplingMode.FAST))
    }

    @Test
    fun selectedSamplingModePersistsAcrossStopAndRestart() = runTest {
        val repository = FakeSensorRepository()
        val viewModel = SensorViewModel(
            observeSensorDataUseCase = ObserveSensorDataUseCase(repository),
            manageSensorSessionUseCase = ManageSensorSessionUseCase(repository)
        )

        viewModel.setCollectionScope(SensorCollectionScope.Detail(SensorType.GYROSCOPE))
        viewModel.startListening()
        advanceUntilIdle()

        viewModel.setSamplingRateFast(true)
        viewModel.stopListening()
        viewModel.startListening()
        advanceUntilIdle()

        assertEquals(SamplingMode.FAST, viewModel.uiState.value.samplingMode)
        assertTrue(repository.observeRequests.contains(SensorType.GYROSCOPE to SamplingMode.FAST))
    }

    @Test
    fun stopAndRestartCreatesNewSessionAndClosesPreviousSession() = runTest {
        val repository = FakeSensorRepository()
        val viewModel = SensorViewModel(
            observeSensorDataUseCase = ObserveSensorDataUseCase(repository),
            manageSensorSessionUseCase = ManageSensorSessionUseCase(repository)
        )

        viewModel.startListening()
        advanceUntilIdle()
        val firstSessionId = viewModel.uiState.value.currentSessionId

        viewModel.stopListening()
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.currentSessionId)
        assertTrue(firstSessionId != null)
        assertTrue(repository.stoppedSessions.contains(firstSessionId))

        viewModel.startListening()
        advanceUntilIdle()
        val secondSessionId = viewModel.uiState.value.currentSessionId

        assertTrue(secondSessionId != null)
        assertTrue(secondSessionId != firstSessionId)
        assertEquals(2, repository.startedSessionIds.size)
    }

    private class FakeSensorRepository : SensorRepository {
        val observeRequests = mutableListOf<Pair<SensorType, SamplingMode>>()
        val startedSessionIds = mutableListOf<Long>()
        val stoppedSessions = mutableListOf<Long>()
        private var nextSessionId = 1L
        private val flows = SensorType.entries.associateWith {
            MutableSharedFlow<SensorData>(extraBufferCapacity = 1)
        }

        override fun availableSensors(): Map<SensorType, Boolean> =
            SensorType.entries.associateWith { true }

        override fun observeSensor(type: SensorType, samplingMode: SamplingMode): Flow<SensorData> {
            observeRequests += type to samplingMode
            return flows.getValue(type)
        }

        override suspend fun startSession(name: String): Long {
            val id = nextSessionId++
            startedSessionIds += id
            return id
        }

        override suspend fun stopSession(sessionId: Long) {
            stoppedSessions += sessionId
        }

        override suspend fun logReading(sessionId: Long, sensorType: SensorType, data: SensorData) = Unit

        override fun observeSessions(): Flow<List<SensorSessionSummary>> = emptyFlow()

        override suspend fun getReadingsForSession(sessionId: Long): List<LoggedReading> = emptyList()
    }
}
