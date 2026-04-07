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
    fun includesSchemaVersionAndUnitsForMixedSensors() {
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
        assertEquals("schemaVersion,sensorType,unit,x,y,z,timestampNanos,recordedAtMillis", lines[0])
        assertTrue(lines[1].startsWith("2,ACCELEROMETER,m/s^2,"))
        assertTrue(lines[2].startsWith("2,LIGHT,lux,"))
    }
}
