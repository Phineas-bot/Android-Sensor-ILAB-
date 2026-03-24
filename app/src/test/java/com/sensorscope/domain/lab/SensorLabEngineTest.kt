package com.sensorscope.domain.lab

import com.sensorscope.core.model.SensorData
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SensorLabEngineTest {

    private val engine = SensorLabEngine()

    @Test
    fun shakeLabCompletesWhenMagnitudeCrossesThreshold() {
        val incomplete = engine.updateShakeLab(SensorData(1f, 1f, 1f, 1L))
        val complete = engine.updateShakeLab(SensorData(16f, 0f, 0f, 2L))

        assertFalse(incomplete.isCompleted)
        assertTrue(complete.isCompleted)
    }

    @Test
    fun magneticNorthCompletesNearZeroDegrees() {
        val nearNorth = engine.updateMagneticNorthLab(SensorData(20f, 0.1f, 0f, 1L))
        val notNorth = engine.updateMagneticNorthLab(SensorData(0f, 20f, 0f, 2L))

        assertTrue(nearNorth.isCompleted)
        assertFalse(notNorth.isCompleted)
    }
}
