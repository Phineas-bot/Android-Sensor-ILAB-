package com.sensorscope.presentation.sensor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        SensorUiState(
            availableSensors = manageSensorSessionUseCase.availableSensors()
        )
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

    fun startListening() {
        if (_uiState.value.isListening) return

        viewModelScope.launch {
            val sessionId = manageSensorSessionUseCase.startSession(
                name = "Session ${System.currentTimeMillis()}"
            )
            _uiState.update { it.copy(isListening = true, currentSessionId = sessionId, errorMessage = null) }
            reconcileCollectors(sessionId)
        }
    }

    fun stopListening() {
        activeJobs.values.forEach { it.cancel() }
        activeJobs.clear()

        val sessionId = _uiState.value.currentSessionId
        if (sessionId != null) {
            viewModelScope.launch {
                manageSensorSessionUseCase.stopSession(sessionId)
            }
        }
        _uiState.update { it.copy(isListening = false, currentSessionId = null) }
    }

    private fun startSensorCollection(type: SensorType, sessionId: Long) {
        if (_uiState.value.availableSensors[type] != true) return

        activeJobs[type] = viewModelScope.launch {
            observeSensorDataUseCase(type).collectLatest { data ->
                onSensorData(type, data)
                manageSensorSessionUseCase.log(sessionId, type, data)
            }
        }
    }

    fun setCollectionScope(scope: SensorCollectionScope) {
        collectionScope = scope
        if (_uiState.value.isListening) {
            val sessionId = _uiState.value.currentSessionId ?: return
            reconcileCollectors(sessionId)
        }
    }

    private fun reconcileCollectors(sessionId: Long) {
        val requiredSensors = collectionScope.requiredSensors()

        val sensorsToStop = activeJobs.keys - requiredSensors
        sensorsToStop.forEach { sensorType ->
            activeJobs.remove(sensorType)?.cancel()
        }

        val sensorsToStart = requiredSensors - activeJobs.keys
        sensorsToStart.forEach { sensorType ->
            startSensorCollection(sensorType, sessionId)
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

        if (type == SensorType.ACCELEROMETER) {
            val shakeLab = labEngine.updateShakeLab(data)
            existing[shakeLab.id] = shakeLab
        }
        if (type == SensorType.MAGNETOMETER) {
            val northLab = labEngine.updateMagneticNorthLab(data)
            existing[northLab.id] = northLab
        }

        _labs.value = existing.values.toList()
    }

    fun setSamplingRateFast(enabled: Boolean) {
        // Sensor delay can be made configurable by extending repository/helper registration.
        _uiState.update {
            it.copy(
                errorMessage = if (enabled) "Fast sampling requested. Restart sensor stream to apply." else null
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopListening()
    }
}
