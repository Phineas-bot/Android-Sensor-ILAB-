package com.sensorscope.core.util

import com.sensorscope.core.model.SensorData
import org.junit.Assert.assertEquals
import org.junit.Test

class SensorRingBufferTest {

    @Test
    fun keepsMostRecentReadingsWithinCapacity() {
        val buffer = SensorRingBuffer(capacity = 3)

        buffer.add(SensorData(1f, 1f, 1f, 1L))
        buffer.add(SensorData(2f, 2f, 2f, 2L))
        buffer.add(SensorData(3f, 3f, 3f, 3L))
        buffer.add(SensorData(4f, 4f, 4f, 4L))

        val list = buffer.toList()

        assertEquals(3, list.size)
        assertEquals(2L, list[0].timestamp)
        assertEquals(3L, list[1].timestamp)
        assertEquals(4L, list[2].timestamp)
    }
}
