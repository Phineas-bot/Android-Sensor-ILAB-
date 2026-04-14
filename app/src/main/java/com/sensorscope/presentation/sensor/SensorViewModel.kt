package com.sensorscope.presentation.sensor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sensorscope.core.model.SamplingMode
import com.sensorscope.core.model.SensorData
import com.sensorscope.core.model.SensorType
import com.sensorscope.core.util.SensorRingBuffer
import com.sensorscope.domain.lab.SensorLab
import com.sensorscope.domain.lab.SensorLabEngine
import com.sensorscope.domain.usecase.ManageSensorSessionUseCase
import com.sensorscope.domain.usecase.ObserveSensorDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SensorViewModel @Inject constructor(
    private val observeSensorDataUseCase: ObserveSensorDataUseCase,
    private val manageSensorSessionUseCase: ManageSensorSessionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SensorUiState(availableSensors = manageSensorSessionUseCase.availableSensors())
    )
    val uiState: StateFlow<SensorUiState> = _uiState.asStateFlow()

    private val _labs = MutableStateFlow<List<SensorLab>>(emptyList())
    val labs: StateFlow<List<SensorLab>> = _labs.asStateFlow()

    private val chartBuffers = SensorType.entries.associateWith { SensorRingBuffer(CHART_BUFFER_CAPACITY) }
    private val activeJobs = mutableMapOf<SensorType, Job>()
    private val labEngine = SensorLabEngine()
    private var collectionScope: SensorCollectionScope = SensorCollectionScope.Dashboard

    companion object {
        private const val CHART_BUFFER_CAPACITY = 180
    }

    // ── Scope management ──────────────────────────────────────────────────────

    /**
     * Called when navigating to a screen. Immediately reconciles which sensors
     * are collecting so charts and labs start updating without a separate button press.
     */
    fun setCollectionScope(scope: SensorCollectionScope) {
        collectionScope = scope
        reconcileCollectors()
    }

    // ── Per-sensor toggle (Dashboard) ─────────────────────────────────────────

    /**
     * Toggle an individual sensor on/off on the Dashboard.
     * Immediately starts or stops collection and, if a session is recording,
     * immediately starts or stops logging for that sensor.
     */
    fun toggleSensor(type: SensorType) {
        val current = _uiState.value.enabledSensors
        val updated = if (type in current) current - type else current + type
        _uiState.update { it.copy(enabledSensors = updated) }
        if (collectionScope is SensorCollectionScope.Dashboard) {
            reconcileCollectors()
        }
    }

    // ── Recording session ─────────────────────────────────────────────────────

    /**
     * Start a DB recording session. All currently-active collectors are restarted
     * so they begin logging to the new session.
     */
    fun startListening() {
        if (_uiState.value.isRecording) return
        viewModelScope.launch {
            val sessionId = manageSensorSessionUseCase.startSession(
                name = "Session ${System.currentTimeMillis()}"
            )
            _uiState.update { it.copy(isRecording = true, currentSessionId = sessionId, errorMessage = null) }
            // Restart active collectors so they pick up the new sessionId and start logging.
            restartActiveCollectors(sessionId)
        }
    }

    /**
     * Stop the current DB recording session. Collectors continue running
     * (charts keep updating) but stop logging to the DB.
     */
    fun stopListening() {
        val sessionId = _uiState.value.currentSessionId
        if (sessionId != null) {
            viewModelScope.launch { manageSensorSessionUseCase.stopSession(sessionId) }
        }
        _uiState.update { it.copy(isRecording = false, currentSessionId = null) }
        // Restart collectors without a session (chart-only mode).
        restartActiveCollectors(sessionId = null)
    }

    // ── Sampling rate ─────────────────────────────────────────────────────────

    fun setSamplingRateFast(enabled: Boolean) {
        val targetMode = if (enabled) SamplingMode.FAST else SamplingMode.NORMAL
        if (_uiState.value.samplingMode == targetMode) return
        _uiState.update { it.copy(samplingMode = targetMode, errorMessage = null) }
        restartActiveCollectors(_uiState.value.currentSessionId)
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    /** Determine which sensors should be active for the current scope. */
    private fun requiredSensors(): Set<SensorType> = when (collectionScope) {
        SensorCollectionScope.Dashboard -> _uiState.value.enabledSensors
        is SensorCollectionScope.Detail -> setOf((collectionScope as SensorCollectionScope.Detail).sensorType)
        SensorCollectionScope.Labs -> SensorCollectionScope.LAB_SENSORS
    }

    /**
     * Start sensors that should be running but aren't, and stop sensors that
     * should not be running. Does NOT restart already-running sensors.
     */
    private fun reconcileCollectors() {
        val required = requiredSensors()
        val sessionId = if (_uiState.value.isRecording) _uiState.value.currentSessionId else null

        (activeJobs.keys - required).forEach { type ->
            activeJobs.remove(type)?.cancel()
        }
        (required - activeJobs.keys).forEach { type ->
            startSensorCollection(type, sessionId)
        }
    }

    /**
     * Cancel every active collector and restart it — used when the recording
     * session changes (start/stop recording, or sampling-rate change).
     */
    private fun restartActiveCollectors(sessionId: Long?) {
        val required = requiredSensors()
        activeJobs.values.forEach { it.cancel() }
        activeJobs.clear()
        required.forEach { type -> startSensorCollection(type, sessionId) }
    }

    /**
     * Launch a coroutine that observes one sensor. If [sessionId] is non-null,
     * each reading is also written to the database.
     */
    private fun startSensorCollection(type: SensorType, sessionId: Long?) {
        if (_uiState.value.availableSensors[type] != true) return
        val samplingMode = _uiState.value.samplingMode

        activeJobs[type] = viewModelScope.launch {
            observeSensorDataUseCase(type, samplingMode).collectLatest { data ->
                onSensorData(type, data)
                if (sessionId != null) {
                    manageSensorSessionUseCase.log(sessionId, type, data)
                }
            }
        }
    }

    private fun onSensorData(type: SensorType, data: SensorData) {
        chartBuffers[type]?.add(data)
        _uiState.update { current ->
            val history = chartBuffers[type]?.toList().orEmpty()
            current.copy(
                latestValues = current.latestValues + (type to data),
                chartSeries = current.chartSeries + (type to history)
            )
        }
        updateLabs(type, data)
    }

    private fun updateLabs(type: SensorType, data: SensorData) {
        val existing = _labs.value.associateBy { it.id }.toMutableMap()

        when (type) {
            SensorType.ACCELEROMETER -> {
                existing[labEngine.shakeChallenge(data).id] = labEngine.shakeChallenge(data)
                existing[labEngine.freeFall(data).id] = labEngine.freeFall(data)
                existing[labEngine.tiltFlat(data).id] = labEngine.tiltFlat(data)
            }
            SensorType.MAGNETOMETER -> {
                existing[labEngine.magneticNorth(data).id] = labEngine.magneticNorth(data)
            }
            SensorType.GYROSCOPE -> {
                existing[labEngine.spinFast(data).id] = labEngine.spinFast(data)
            }
            SensorType.LIGHT -> {
                existing[labEngine.brightLight(data).id] = labEngine.brightLight(data)
            }
            else -> Unit
        }
        _labs.value = existing.values.toList()
    }

    override fun onCleared() {
        super.onCleared()
        activeJobs.values.forEach { it.cancel() }
        activeJobs.clear()
        val sessionId = _uiState.value.currentSessionId
        if (sessionId != null) {
            viewModelScope.launch { manageSensorSessionUseCase.stopSession(sessionId) }
        }
    }
}
