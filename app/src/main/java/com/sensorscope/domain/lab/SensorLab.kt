package com.sensorscope.domain.lab

import com.sensorscope.core.model.SensorData
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

enum class LabId {
    SHAKE_CHALLENGE,
    MAGNETIC_NORTH,
    FREE_FALL,
    TILT_FLAT,
    SPIN_FAST,
    BRIGHT_LIGHT
}

data class SensorLab(
    val id: LabId,
    val emoji: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val progressText: String = "",
    /** 0.0 – 1.0 fraction used to draw the progress bar. */
    val progress: Float = 0f
)

class SensorLabEngine {

    // ── Accelerometer labs ────────────────────────────────────────────────────

    fun shakeChallenge(data: SensorData): SensorLab {
        val threshold = 15f
        val magnitude = magnitude3(data)
        val progress = (magnitude / threshold).coerceIn(0f, 1f)
        return SensorLab(
            id = LabId.SHAKE_CHALLENGE,
            emoji = "📳",
            title = "Shake Challenge",
            description = "Shake the device hard enough to reach ${threshold.toInt()} m/s² total acceleration.",
            isCompleted = magnitude >= threshold,
            progressText = "Magnitude: ${"%.2f".format(magnitude)} m/s²  (target ≥ ${threshold.toInt()})",
            progress = progress
        )
    }

    fun freeFall(data: SensorData): SensorLab {
        val threshold = 2f       // m/s² — near-weightless
        val magnitude = magnitude3(data)
        // Progress goes up as magnitude goes DOWN toward 0
        val progress = (1f - (magnitude / 9.81f)).coerceIn(0f, 1f)
        return SensorLab(
            id = LabId.FREE_FALL,
            emoji = "🪂",
            title = "Free Fall",
            description = "Create near-weightlessness: toss the device gently upward or use a drop test to get acceleration below ${threshold} m/s².",
            isCompleted = magnitude < threshold,
            progressText = "Magnitude: ${"%.2f".format(magnitude)} m/s²  (target < $threshold)",
            progress = progress
        )
    }

    fun tiltFlat(data: SensorData): SensorLab {
        // Flat = Z ≈ 9.81, X ≈ 0, Y ≈ 0
        val xDev = abs(data.x)
        val yDev = abs(data.y)
        val zDev = abs(abs(data.z) - 9.81f)
        val totalDev = xDev + yDev + zDev
        val completed = xDev < 0.4f && yDev < 0.4f && zDev < 0.3f
        val progress = (1f - (totalDev / 5f)).coerceIn(0f, 1f)
        return SensorLab(
            id = LabId.TILT_FLAT,
            emoji = "⚖️",
            title = "Spirit Level",
            description = "Hold the device perfectly flat on a surface (Z ≈ 9.81 m/s², X ≈ 0, Y ≈ 0).",
            isCompleted = completed,
            progressText = "X: ${"%.2f".format(data.x)}  Y: ${"%.2f".format(data.y)}  Z: ${"%.2f".format(data.z)} m/s²",
            progress = progress
        )
    }

    // ── Magnetometer labs ─────────────────────────────────────────────────────

    fun magneticNorth(data: SensorData): SensorLab {
        val threshold = 20f
        val heading = Math.toDegrees(atan2(data.y.toDouble(), data.x.toDouble())).toFloat()
        val normalized = if (heading < 0) heading + 360f else heading
        val deviation = if (normalized > 180f) 360f - normalized else normalized
        val completed = deviation <= threshold
        val progress = (1f - (deviation / 180f)).coerceIn(0f, 1f)
        return SensorLab(
            id = LabId.MAGNETIC_NORTH,
            emoji = "🧭",
            title = "Magnetic North Hunt",
            description = "Point the device toward magnetic north — keep heading within ±${threshold.toInt()}° of 0°.",
            isCompleted = completed,
            progressText = "Heading: ${"%.1f".format(normalized)}°  (deviation: ${"%.1f".format(deviation)}°)",
            progress = progress
        )
    }

    // ── Gyroscope labs ────────────────────────────────────────────────────────

    fun spinFast(data: SensorData): SensorLab {
        val threshold = 5f   // rad/s
        val magnitude = magnitude3(data)
        val progress = (magnitude / threshold).coerceIn(0f, 1f)
        return SensorLab(
            id = LabId.SPIN_FAST,
            emoji = "🌀",
            title = "Spin Cycle",
            description = "Spin the device fast enough to reach ${threshold.toInt()} rad/s rotational speed.",
            isCompleted = magnitude >= threshold,
            progressText = "Rotation: ${"%.2f".format(magnitude)} rad/s  (target ≥ $threshold)",
            progress = progress
        )
    }

    // ── Light labs ────────────────────────────────────────────────────────────

    fun brightLight(data: SensorData): SensorLab {
        val threshold = 500f   // lux
        val lux = data.x
        val progress = (lux / threshold).coerceIn(0f, 1f)
        return SensorLab(
            id = LabId.BRIGHT_LIGHT,
            emoji = "☀️",
            title = "Bright Spot",
            description = "Aim the light sensor at a bright source to exceed ${threshold.toInt()} lux.",
            isCompleted = lux >= threshold,
            progressText = "Light: ${"%.1f".format(lux)} lux  (target ≥ ${threshold.toInt()})",
            progress = progress
        )
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private fun magnitude3(d: SensorData) = sqrt(d.x * d.x + d.y * d.y + d.z * d.z)
}
