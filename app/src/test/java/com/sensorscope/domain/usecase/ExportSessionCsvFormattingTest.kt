package com.sensorscope.domain.usecase

import com.sensorscope.core.model.SensorType
import com.sensorscope.domain.repository.LoggedReading
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExportSessionCsvFormattingTest {

    @Test
    fun returnsNullForEmptyReadings() {
        val csv = buildCsvContent(emptyList())

        assertEquals(null, csv)
    }

    @Test
    fun includesFriendlyColumnsAndReadableValuesForMixedSensors() {
        val readings = listOf(
            LoggedReading(
                sensorType = SensorType.ACCELEROMETER,
                x = 1.1f,
                y = 2.2f,
                z = 3.3f,
                timestampNanos = 1000L,
                recordedAtMillis = 2000L
            ),
            LoggedReading(
                sensorType = SensorType.LIGHT,
                x = 120.5f,
                y = 0f,
                z = 0f,
                timestampNanos = 3000L,
                recordedAtMillis = 4000L
            )
        )

        val csv = buildCsvContent(readings)

        assertTrue(csv != null)
        val lines = csv!!.lines()
        assertEquals("schemaVersion,recordedAt,sensor,unit,primaryValue,readingSummary,timestampNanos,recordedAtMillis", lines[0])
        assertTrue(lines[1].startsWith("3,"))
        assertTrue(lines[1].contains(",Accelerometer,m/s^2,1.100,"))
        assertTrue(lines[1].contains("x=1.100 m/s^2, y=2.200 m/s^2, z=3.300 m/s^2"))
        assertTrue(lines[2].contains(",Light,lux,120.500,120.500 lux,"))
    }
}
