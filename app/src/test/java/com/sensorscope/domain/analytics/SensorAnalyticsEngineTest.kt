package com.sensorscope.domain.analytics

import com.sensorscope.core.model.SensorData
import com.sensorscope.core.model.SensorType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SensorAnalyticsEngineTest {

    private val engine = SensorAnalyticsEngine()

    @Test
    fun trendSummary_detectsRisingSeries() {
        val series = (1..12).map {
            SensorData(x = it.toFloat(), y = 0f, z = 0f, timestamp = it.toLong())
        }

        val summary = engine.calculateTrendSummary(SensorType.LIGHT, series)

        assertNotNull(summary)
        assertEquals("Rising", summary?.direction)
        assertTrue((summary?.average ?: 0f) > 6f)
    }

    @Test
    fun crossSensorInsights_reportsMotionCoupling() {
        val accel = (1..20).map {
            SensorData(x = it.toFloat(), y = 0f, z = 0f, timestamp = it.toLong())
        }
        val gyro = (1..20).map {
            SensorData(x = (it * 2).toFloat(), y = 0f, z = 0f, timestamp = it.toLong())
        }

        val insights = engine.crossSensorInsights(
            mapOf(
                SensorType.ACCELEROMETER to accel,
                SensorType.GYROSCOPE to gyro
            )
        )

        assertTrue(insights.any { it.contains("Motion coupling") })
    }
}
