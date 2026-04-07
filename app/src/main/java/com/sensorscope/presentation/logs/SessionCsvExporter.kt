package com.sensorscope.presentation.logs

import android.net.Uri
import com.sensorscope.domain.usecase.ExportSessionCsvUseCase
import javax.inject.Inject

interface SessionCsvExporter {
    suspend fun export(sessionId: Long): Uri?
}

class SessionCsvExporterImpl @Inject constructor(
    private val exportSessionCsvUseCase: ExportSessionCsvUseCase
) : SessionCsvExporter {
    override suspend fun export(sessionId: Long): Uri? = exportSessionCsvUseCase(sessionId)
}