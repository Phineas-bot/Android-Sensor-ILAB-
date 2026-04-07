package com.sensorscope.presentation.logs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sensorscope.domain.model.SensorSessionSummary
import com.sensorscope.domain.repository.SensorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LogsViewModel @Inject constructor(
    private val repository: SensorRepository,
    private val sessionCsvExporter: SessionCsvExporter
) : ViewModel() {

    private val _sessions = MutableStateFlow<List<SensorSessionSummary>>(emptyList())
    val sessions: StateFlow<List<SensorSessionSummary>> = _sessions.asStateFlow()

    private val _exportedUri = MutableStateFlow<String?>(null)
    val exportedUri: StateFlow<String?> = _exportedUri.asStateFlow()

    private val _exportMessage = MutableStateFlow<String?>(null)
    val exportMessage: StateFlow<String?> = _exportMessage.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeSessions().collect { sessions ->
                _sessions.value = sessions
            }
        }
    }

    fun exportSession(sessionId: Long) {
        viewModelScope.launch {
            _exportMessage.value = "Exporting CSV..."

            val uri = runCatching {
                exportWithRetry(sessionId)
            }.getOrElse {
                _exportMessage.value = "Export failed. Please try again."
                null
            }

            if (uri != null) {
                _exportedUri.value = uri.toString()
                _exportMessage.value = "CSV ready. Opening share sheet..."
            } else if (_exportMessage.value == "Exporting CSV...") {
                _exportMessage.value = "No readings available for this session."
            }
        }
    }

    private suspend fun exportWithRetry(sessionId: Long) =
        sessionCsvExporter.export(sessionId) ?: sessionCsvExporter.export(sessionId)

    fun clearExportState() {
        _exportedUri.value = null
    }

    fun clearExportMessage() {
        _exportMessage.value = null
    }
}
