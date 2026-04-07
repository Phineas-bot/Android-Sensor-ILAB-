package com.sensorscope.domain.usecase

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.sensorscope.domain.repository.LoggedReading
import com.sensorscope.domain.repository.SensorRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

internal const val CSV_SCHEMA_VERSION = 2

internal fun buildCsvContent(readings: List<LoggedReading>): String? {
    if (readings.isEmpty()) return null

    val header = "schemaVersion,sensorType,unit,x,y,z,timestampNanos,recordedAtMillis\n"
    val rows = readings.joinToString(separator = "\n") {
        "$CSV_SCHEMA_VERSION,${it.sensorType.name},${it.sensorType.unit},${it.x},${it.y},${it.z},${it.timestampNanos},${it.recordedAtMillis}"
    }
    return header + rows
}

class ExportSessionCsvUseCase @Inject constructor(
    private val repository: SensorRepository,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(sessionId: Long): Uri? {
        val readings = repository.getReadingsForSession(sessionId)
        val csvContent = buildCsvContent(readings) ?: return null

        val fileName = "sensorscope_session_${sessionId}_${System.currentTimeMillis()}.csv"

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            exportWithMediaStore(fileName, csvContent)
        } else {
            exportWithFileProvider(fileName, csvContent)
        }
    }

    private fun exportWithMediaStore(fileName: String, content: String): Uri? {
        val resolver = context.contentResolver
        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "text/csv")
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values) ?: return null

        resolver.openOutputStream(uri)?.bufferedWriter()?.use { writer ->
            writer.write(content)
        }
        return uri
    }

    private fun exportWithFileProvider(fileName: String, content: String): Uri {
        val file = File(context.cacheDir, fileName)
        FileOutputStream(file).bufferedWriter().use { writer ->
            writer.write(content)
        }
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }
}
