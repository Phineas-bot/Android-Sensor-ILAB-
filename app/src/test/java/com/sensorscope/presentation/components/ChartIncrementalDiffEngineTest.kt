package com.sensorscope.presentation.components

import com.sensorscope.core.model.SensorData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ChartIncrementalDiffEngineTest {

    @Test
    fun emitsResetAndAllPointsOnFirstFrame() {
        val engine = ChartIncrementalDiffEngine()
        val points = listOf(
            SensorData(1f, 2f, 3f, 1L),
            SensorData(4f, 5f, 6f, 2L)
        )

        val delta = engine.nextDelta(points)

        assertTrue(delta.reset)
        assertEquals(2, delta.appended.size)
        assertEquals(2, delta.maxWindowSize)
    }

    @Test
    fun emitsOnlyNewTailPointsAfterFirstFrame() {
        val engine = ChartIncrementalDiffEngine()

        engine.nextDelta(
            listOf(
                SensorData(1f, 2f, 3f, 1L),
                SensorData(4f, 5f, 6f, 2L)
            )
        )

        val next = engine.nextDelta(
            listOf(
                SensorData(1f, 2f, 3f, 1L),
                SensorData(4f, 5f, 6f, 2L),
                SensorData(7f, 8f, 9f, 3L)
            )
        )

        assertFalse(next.reset)
        assertEquals(1, next.appended.size)
        assertEquals(3L, next.appended.first().timestamp)
    }

    @Test
    fun requestsResetWhenHistoryNoLongerContainsPreviousTail() {
        val engine = ChartIncrementalDiffEngine()

        engine.nextDelta(
            listOf(
                SensorData(1f, 2f, 3f, 100L),
                SensorData(4f, 5f, 6f, 200L)
            )
        )

        val next = engine.nextDelta(
            listOf(
                SensorData(7f, 8f, 9f, 300L),
                SensorData(10f, 11f, 12f, 400L)
            )
        )

        assertTrue(next.reset)
        assertEquals(2, next.appended.size)
    }
}
