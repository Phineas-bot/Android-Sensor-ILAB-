package com.sensorscope.domain.lab

import com.sensorscope.core.model.SensorData
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

enum class LabId { SHAKE_CHALLENGE, MAGNETIC_NORTH }

data class SensorLab(
    val id: LabId,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val progressText: String = ""
)

class SensorLabEngine {
    private val shakeThreshold = 15f
    private val northThresholdDegrees = 20f

    fun updateShakeLab(accelerometer: SensorData): SensorLab {
        val magnitude = sqrt(
            accelerometer.x * accelerometer.x +
                accelerometer.y * accelerometer.y +
                accelerometer.z * accelerometer.z
        )
        val completed = magnitude >= shakeThreshold
        return SensorLab(
            id = LabId.SHAKE_CHALLENGE,
            title = "Shake phone to reach X acceleration",
            description = "Generate acceleration magnitude >= ${shakeThreshold} m/s^2",
            isCompleted = completed,
            progressText = "Current magnitude: ${"%.2f".format(magnitude)}"
        )
    }

    fun updateMagneticNorthLab(magnetometer: SensorData): SensorLab {
        val heading = Math.toDegrees(atan2(magnetometer.y.toDouble(), magnetometer.x.toDouble())).toFloat()
        val normalized = if (heading < 0) heading + 360f else heading
        val completed = normalized <= northThresholdDegrees || normalized >= (360f - northThresholdDegrees)

        return SensorLab(
            id = LabId.MAGNETIC_NORTH,
            title = "Detect magnetic north",
            description = "Keep heading near 0° (+/- ${northThresholdDegrees.toInt()}°)",
            isCompleted = completed,
            progressText = "Heading: ${"%.1f".format(abs(normalized))}°"
        )
    }
}
