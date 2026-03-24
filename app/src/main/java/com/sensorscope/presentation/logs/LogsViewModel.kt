package com.sensorscope.presentation.logs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sensorscope.domain.model.SensorSessionSummary
import com.sensorscope.domain.repository.SensorRepository
import com.sensorscope.domain.usecase.ExportSessionCsvUseCase
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
    private val exportSessionCsvUseCase: ExportSessionCsvUseCase
) : ViewModel() {

    private val _sessions = MutableStateFlow<List<SensorSessionSummary>>(emptyList())
    val sessions: StateFlow<List<SensorSessionSummary>> = _sessions.asStateFlow()

    private val _exportedUri = MutableStateFlow<String?>(null)
    val exportedUri: StateFlow<String?> = _exportedUri.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeSessions().collect { sessions ->
                _sessions.value = sessions
            }
        }
    }

    fun exportSession(sessionId: Long) {
        viewModelScope.launch {
            val uri = exportSessionCsvUseCase(sessionId)
            _exportedUri.update { uri?.toString() }
        }
    }

    fun clearExportState() {
        _exportedUri.value = null
    }
}
