package com.sensorscope.presentation.sensor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sensorscope.core.model.SamplingMode
import com.sensorscope.core.model.SensorData
import com.sensorscope.core.model.SensorType
import com.sensorscope.core.util.SensorRingBuffer
import com.sensorscope.domain.analytics.SensorAnalyticsEngine
import com.sensorscope.domain.lab.LabId
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
    private val manageSensorSessionUseCase: ManageSensorSessionUseCase,
    private val analyticsEngine: SensorAnalyticsEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SensorUiState(
            availableSensors = manageSensorSessionUseCase.availableSensors()
        )
    )
    val uiState: StateFlow<SensorUiState> = _uiState.asStateFlow()

    private val chartBuffers = SensorType.entries.associateWith { SensorRingBuffer(CHART_BUFFER_CAPACITY) }
    private val activeJobs = mutableMapOf<SensorType, Job>()
    private val labEngine = SensorLabEngine()
    private val labTemplates = labEngine.initialLabs()
    private val labTemplateMap = labTemplates.associateBy { it.id }
    private val _labs = MutableStateFlow(labTemplates)
    val labs: StateFlow<List<SensorLab>> = _labs.asStateFlow()
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
        val samplingMode = _uiState.value.samplingMode

        activeJobs[type] = viewModelScope.launch {
            observeSensorDataUseCase(type, samplingMode).collectLatest { data ->
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
            val nextSeries = current.chartSeries + (type to history)
            val nextTrends = nextSeries.mapNotNull { (sensor, series) ->
                analyticsEngine.calculateTrendSummary(sensor, series)?.let { sensor to it }
            }.toMap()
            val nextInsights = analyticsEngine.crossSensorInsights(nextSeries)

            current.copy(
                latestValues = current.latestValues + (type to data),
                chartSeries = nextSeries,
                trendSummaries = nextTrends,
                crossSensorInsights = nextInsights
            )
        }

        updateLabs(type, data)
    }

    private fun updateLabs(type: SensorType, data: SensorData) {
        val existing = _labs.value.associateBy { it.id }.toMutableMap()
        val updates = labEngine.evaluate(type, data)

        updates.forEach { updatedLab ->
            val previous = existing[updatedLab.id]
            existing[updatedLab.id] = when {
                previous?.isCompleted == true -> updatedLab.copy(
                    isCompleted = true,
                    progressText = previous.progressText
                )
                updatedLab.isCompleted -> updatedLab.copy(progressText = "Completed. ${updatedLab.progressText}")
                else -> updatedLab
            }
        }

        _labs.value = labTemplates.map { template -> existing[template.id] ?: template }
    }

    fun relaunchLab(id: LabId) {
        labEngine.onRelaunch(id)
        val template = labTemplateMap[id] ?: return
        _labs.update { current ->
            current.map { lab ->
                if (lab.id == id) template else lab
            }
        }
    }

    fun setSamplingRateFast(enabled: Boolean) {
        val targetMode = if (enabled) SamplingMode.FAST else SamplingMode.NORMAL
        if (_uiState.value.samplingMode == targetMode) return

        _uiState.update { it.copy(samplingMode = targetMode, errorMessage = null) }

        val sessionId = _uiState.value.currentSessionId ?: return
        if (_uiState.value.isListening) {
            activeJobs.values.forEach { it.cancel() }
            activeJobs.clear()
            reconcileCollectors(sessionId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopListening()
    }
}
