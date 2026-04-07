package com.sensorscope.domain.analytics

import com.sensorscope.core.model.AxisModel
import com.sensorscope.core.model.SensorData
import com.sensorscope.core.model.SensorType
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.pow
import kotlin.math.sqrt

data class SensorTrendSummary(
    val average: Float,
    val minimum: Float,
    val maximum: Float,
    val delta: Float,
    val direction: String
)

@Singleton
class SensorAnalyticsEngine @Inject constructor() {

    fun calculateTrendSummary(type: SensorType, series: List<SensorData>): SensorTrendSummary? {
        if (series.size < 2) return null
        val values = series.map { toMagnitude(type, it) }
        val avg = values.average().toFloat()
        val min = values.minOrNull() ?: return null
        val max = values.maxOrNull() ?: return null
        val delta = values.last() - values.first()
        val direction = when {
            delta > 0.15f -> "Rising"
            delta < -0.15f -> "Falling"
            else -> "Stable"
        }
        return SensorTrendSummary(
            average = avg,
            minimum = min,
            maximum = max,
            delta = delta,
            direction = direction
        )
    }

    fun crossSensorInsights(seriesBySensor: Map<SensorType, List<SensorData>>): List<String> {
        val insights = mutableListOf<String>()

        val accel = seriesBySensor[SensorType.ACCELEROMETER].orEmpty()
        val gyro = seriesBySensor[SensorType.GYROSCOPE].orEmpty()
        val corr = correlation(
            accel.map { toMagnitude(SensorType.ACCELEROMETER, it) },
            gyro.map { toMagnitude(SensorType.GYROSCOPE, it) }
        )
        if (corr != null) {
            val quality = when {
                corr >= 0.7f -> "high"
                corr >= 0.4f -> "moderate"
                else -> "low"
            }
            insights += "Motion coupling (accelerometer vs gyroscope): $quality (${format(corr)})"
        }

        val audio = seriesBySensor[SensorType.AUDIO_LEVEL].orEmpty().lastOrNull()?.x
        val ble = seriesBySensor[SensorType.BLUETOOTH_SIGNAL].orEmpty().lastOrNull()?.x
        if (audio != null && ble != null) {
            insights += "Ambient context: audio ${format(audio)} dBFS, BLE ${format(ble)} dBm"
        }

        val light = seriesBySensor[SensorType.LIGHT].orEmpty().lastOrNull()?.x
        val pressure = seriesBySensor[SensorType.PRESSURE].orEmpty().lastOrNull()?.x
        if (light != null && pressure != null) {
            insights += "Environment context: ${format(light)} lux, ${format(pressure)} hPa"
        }

        return insights.take(4)
    }

    private fun toMagnitude(type: SensorType, sample: SensorData): Float {
        return if (type.capability.axisModel == AxisModel.TRI_AXIS) {
            sqrt(sample.x.pow(2) + sample.y.pow(2) + sample.z.pow(2))
        } else {
            sample.x
        }
    }

    private fun correlation(a: List<Float>, b: List<Float>): Float? {
        val n = minOf(a.size, b.size)
        if (n < 8) return null

        val x = a.takeLast(n)
        val y = b.takeLast(n)

        val meanX = x.average().toFloat()
        val meanY = y.average().toFloat()

        var cov = 0f
        var varX = 0f
        var varY = 0f

        for (i in 0 until n) {
            val dx = x[i] - meanX
            val dy = y[i] - meanY
            cov += dx * dy
            varX += dx * dx
            varY += dy * dy
        }

        if (varX == 0f || varY == 0f) return null
        return (cov / sqrt(varX * varY)).coerceIn(-1f, 1f)
    }

    private fun format(value: Float): String = "%.2f".format(value)
}
