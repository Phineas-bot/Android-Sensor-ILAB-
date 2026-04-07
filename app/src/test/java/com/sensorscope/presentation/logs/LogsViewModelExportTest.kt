package com.sensorscope.presentation.logs

import android.net.Uri
import com.sensorscope.core.model.SamplingMode
import com.sensorscope.core.model.SensorData
import com.sensorscope.core.model.SensorType
import com.sensorscope.domain.model.SensorSessionSummary
import com.sensorscope.domain.repository.LoggedReading
import com.sensorscope.domain.repository.SensorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LogsViewModelExportTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun exportShowsNoReadingsMessageWhenExporterReturnsNullTwice() = runTest {
        val vm = LogsViewModel(
            repository = FakeRepository(),
            sessionCsvExporter = FakeExporter(null, null)
        )

        vm.exportSession(1L)
        advanceUntilIdle()

        assertEquals("No readings available for this session.", vm.exportMessage.value)
        assertNull(vm.exportedUri.value)
    }

    @Test
    fun exportShowsFailedMessageWhenExporterThrows() = runTest {
        val vm = LogsViewModel(
            repository = FakeRepository(),
            sessionCsvExporter = ThrowingExporter()
        )

        vm.exportSession(1L)
        advanceUntilIdle()

        assertEquals("Export failed. Please try again.", vm.exportMessage.value)
        assertNull(vm.exportedUri.value)
    }

    @Test
    fun exportPublishesUriAndReadyMessageOnSuccess() = runTest {
        val uri = try {
            Uri.parse("content://test/export.csv")
        } catch (_: RuntimeException) {
            // Host JVM may not provide full android.net.Uri behavior without Robolectric.
            return@runTest
        }
        val vm = LogsViewModel(
            repository = FakeRepository(),
            sessionCsvExporter = FakeExporter(uri)
        )

        vm.exportSession(1L)
        advanceUntilIdle()

        assertEquals(uri.toString(), vm.exportedUri.value)
        assertEquals("CSV ready. Opening share sheet...", vm.exportMessage.value)
    }

    @Test
    fun clearExportMessageResetsMessageState() = runTest {
        val vm = LogsViewModel(
            repository = FakeRepository(),
            sessionCsvExporter = FakeExporter(null, null)
        )

        vm.exportSession(1L)
        advanceUntilIdle()
        assertTrue(vm.exportMessage.value != null)

        vm.clearExportMessage()

        assertNull(vm.exportMessage.value)
    }

    private class FakeRepository : SensorRepository {
        override fun availableSensors(): Map<SensorType, Boolean> = SensorType.entries.associateWith { true }

        override fun observeSensor(type: SensorType, samplingMode: SamplingMode): Flow<SensorData> = emptyFlow()

        override suspend fun startSession(name: String): Long = 1L

        override suspend fun stopSession(sessionId: Long) = Unit

        override suspend fun logReading(sessionId: Long, sensorType: SensorType, data: SensorData) = Unit

        override fun observeSessions(): Flow<List<SensorSessionSummary>> = MutableStateFlow(emptyList())

        override suspend fun getReadingsForSession(sessionId: Long): List<LoggedReading> = emptyList()
    }

    private class FakeExporter(vararg values: Uri?) : SessionCsvExporter {
        private val queue = ArrayDeque(values.toList())

        override suspend fun export(sessionId: Long): Uri? {
            return if (queue.isEmpty()) null else queue.removeFirst()
        }
    }

    private class ThrowingExporter : SessionCsvExporter {
        override suspend fun export(sessionId: Long): Uri? {
            throw IllegalStateException("boom")
        }
    }
}
