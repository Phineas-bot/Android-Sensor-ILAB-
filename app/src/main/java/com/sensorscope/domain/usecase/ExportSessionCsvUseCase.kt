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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

internal const val CSV_SCHEMA_VERSION = 3

internal fun buildCsvContent(readings: List<LoggedReading>): String? {
    if (readings.isEmpty()) return null

    val header = "schemaVersion,recordedAt,sensor,unit,primaryValue,readingSummary,timestampNanos,recordedAtMillis\n"
    val rows = readings.joinToString(separator = "\n") {
        val sensor = it.sensorType
        val unit = sensor.unit
        val primaryValue = formatDecimal(it.x)
        val summary = if (sensor.axisCount == 1) {
            "$primaryValue $unit"
        } else {
            "x=${formatDecimal(it.x)} $unit, y=${formatDecimal(it.y)} $unit, z=${formatDecimal(it.z)} $unit"
        }

        listOf(
            CSV_SCHEMA_VERSION.toString(),
            csvEscape(formatRecordedAt(it.recordedAtMillis)),
            csvEscape(sensor.displayName),
            csvEscape(unit),
            primaryValue,
            csvEscape(summary),
            it.timestampNanos.toString(),
            it.recordedAtMillis.toString()
        ).joinToString(",")
    }
    return header + rows
}

private fun formatRecordedAt(recordedAtMillis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)
    return formatter.format(Date(recordedAtMillis))
}

private fun formatDecimal(value: Float): String {
    return String.format(Locale.US, "%.3f", value)
}

private fun csvEscape(value: String): String {
    if (value.none { it == ',' || it == '"' || it == '\n' || it == '\r' }) return value
    return "\"${value.replace("\"", "\"\"")}\""
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
