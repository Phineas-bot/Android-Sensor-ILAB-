package com.sensorscope.domain.usecase

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.sensorscope.domain.repository.SensorRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ExportSessionCsvUseCase @Inject constructor(
    private val repository: SensorRepository,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(sessionId: Long): Uri? {
        val readings = repository.getReadingsForSession(sessionId)
        if (readings.isEmpty()) return null

        val fileName = "sensorscope_session_${sessionId}_${System.currentTimeMillis()}.csv"
        val header = "sensorType,x,y,z,timestampNanos,recordedAtMillis\n"
        val rows = readings.joinToString(separator = "\n") {
            "${it.sensorType.name},${it.x},${it.y},${it.z},${it.timestampNanos},${it.recordedAtMillis}"
        }
        val csvContent = header + rows

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
