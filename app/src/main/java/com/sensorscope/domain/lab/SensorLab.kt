package com.sensorscope.domain.lab

import com.sensorscope.core.model.SensorData
import com.sensorscope.core.model.SensorType
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

enum class LabId {
    SHAKE_CHALLENGE,
    MAGNETIC_NORTH,
    GYRO_STABILITY,
    BRIGHT_LIGHT_DETECTION,
    PROXIMITY_TRIGGER,
    PRESSURE_SHIFT,
    AUDIO_QUIET_ZONE,
    AUDIO_PEAK_EVENT,
    BLUETOOTH_PROXIMITY,
    CAMERA_CAPABILITY_CHECK
}

data class SensorLab(
    val id: LabId,
    val title: String,
    val description: String,
    val sourceSensor: SensorType,
    val isCompleted: Boolean = false,
    val progressText: String = ""
)

class SensorLabEngine {
    private val shakeThreshold = 15f
    private val northThresholdDegrees = 20f
    private val gyroStableThreshold = 0.7f
    private val brightLuxThreshold = 300f
    private val proximityNearThreshold = 3f
    private val pressureShiftThreshold = 3f
    private val quietDbfsThreshold = -35f
    private val peakDbfsThreshold = -12f
    private val bluetoothNearThreshold = -60f
    private val cameraCapabilityThreshold = 35f

    private var pressureBaseline: Float? = null

    fun initialLabs(): List<SensorLab> = listOf(
        SensorLab(
            id = LabId.SHAKE_CHALLENGE,
            title = "Shake phone to reach high acceleration",
            description = "Generate acceleration magnitude >= ${shakeThreshold} m/s^2",
            sourceSensor = SensorType.ACCELEROMETER,
            progressText = "Awaiting accelerometer input"
        ),
        SensorLab(
            id = LabId.MAGNETIC_NORTH,
            title = "Detect magnetic north",
            description = "Keep heading near 0° (+/- ${northThresholdDegrees.toInt()}°)",
            sourceSensor = SensorType.MAGNETOMETER,
            progressText = "Awaiting magnetometer input"
        ),
        SensorLab(
            id = LabId.GYRO_STABILITY,
            title = "Gyroscope stability check",
            description = "Keep rotational motion below ${gyroStableThreshold} rad/s",
            sourceSensor = SensorType.GYROSCOPE,
            progressText = "Awaiting gyroscope input"
        ),
        SensorLab(
            id = LabId.BRIGHT_LIGHT_DETECTION,
            title = "Bright-light detection",
            description = "Reach at least ${brightLuxThreshold.toInt()} lux",
            sourceSensor = SensorType.LIGHT,
            progressText = "Awaiting light sensor input"
        ),
        SensorLab(
            id = LabId.PROXIMITY_TRIGGER,
            title = "Near-object detection",
            description = "Bring an object within ${proximityNearThreshold} cm",
            sourceSensor = SensorType.PROXIMITY,
            progressText = "Awaiting proximity input"
        ),
        SensorLab(
            id = LabId.PRESSURE_SHIFT,
            title = "Pressure shift challenge",
            description = "Change pressure by at least ${pressureShiftThreshold} hPa from baseline",
            sourceSensor = SensorType.PRESSURE,
            progressText = "Awaiting pressure baseline"
        ),
        SensorLab(
            id = LabId.AUDIO_QUIET_ZONE,
            title = "Quiet-zone detection",
            description = "Reduce audio level to <= ${quietDbfsThreshold.toInt()} dBFS",
            sourceSensor = SensorType.AUDIO_LEVEL,
            progressText = "Awaiting microphone input"
        ),
        SensorLab(
            id = LabId.AUDIO_PEAK_EVENT,
            title = "Audio peak event",
            description = "Reach audio level >= ${peakDbfsThreshold.toInt()} dBFS",
            sourceSensor = SensorType.AUDIO_LEVEL,
            progressText = "Awaiting microphone input"
        ),
        SensorLab(
            id = LabId.BLUETOOTH_PROXIMITY,
            title = "Bluetooth proximity",
            description = "Reach BLE signal stronger than ${bluetoothNearThreshold.toInt()} dBm",
            sourceSensor = SensorType.BLUETOOTH_SIGNAL,
            progressText = "Awaiting Bluetooth scan input"
        ),
        SensorLab(
            id = LabId.CAMERA_CAPABILITY_CHECK,
            title = "Camera capability check",
            description = "Reach camera metadata score >= ${cameraCapabilityThreshold.toInt()}",
            sourceSensor = SensorType.CAMERA_METADATA,
            progressText = "Awaiting camera metadata input"
        )
    )

    fun evaluate(sensorType: SensorType, data: SensorData): List<SensorLab> {
        return when (sensorType) {
            SensorType.ACCELEROMETER -> listOf(updateShakeLab(data))
            SensorType.MAGNETOMETER -> listOf(updateMagneticNorthLab(data))
            SensorType.GYROSCOPE -> listOf(updateGyroStabilityLab(data))
            SensorType.LIGHT -> listOf(updateBrightLightLab(data))
            SensorType.PROXIMITY -> listOf(updateProximityLab(data))
            SensorType.PRESSURE -> listOf(updatePressureShiftLab(data))
            SensorType.AUDIO_LEVEL -> listOf(updateAudioQuietLab(data), updateAudioPeakLab(data))
            SensorType.BLUETOOTH_SIGNAL -> listOf(updateBluetoothProximityLab(data))
            SensorType.CAMERA_METADATA -> listOf(updateCameraCapabilityLab(data))
        }
    }

    fun onRelaunch(id: LabId) {
        if (id == LabId.PRESSURE_SHIFT) {
            pressureBaseline = null
        }
    }

    fun updateShakeLab(accelerometer: SensorData): SensorLab {
        val magnitude = sqrt(
            accelerometer.x * accelerometer.x +
                accelerometer.y * accelerometer.y +
                accelerometer.z * accelerometer.z
        )
        val completed = magnitude >= shakeThreshold
        return SensorLab(
            id = LabId.SHAKE_CHALLENGE,
            title = "Shake phone to reach high acceleration",
            description = "Generate acceleration magnitude >= ${shakeThreshold} m/s^2",
            sourceSensor = SensorType.ACCELEROMETER,
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
            sourceSensor = SensorType.MAGNETOMETER,
            isCompleted = completed,
            progressText = "Heading: ${"%.1f".format(abs(normalized))}°"
        )
    }

    private fun updateGyroStabilityLab(gyroscope: SensorData): SensorLab {
        val magnitude = sqrt(
            gyroscope.x * gyroscope.x +
                gyroscope.y * gyroscope.y +
                gyroscope.z * gyroscope.z
        )
        val completed = magnitude <= gyroStableThreshold
        return SensorLab(
            id = LabId.GYRO_STABILITY,
            title = "Gyroscope stability check",
            description = "Keep rotational motion below ${gyroStableThreshold} rad/s",
            sourceSensor = SensorType.GYROSCOPE,
            isCompleted = completed,
            progressText = "Current rotation magnitude: ${"%.2f".format(magnitude)}"
        )
    }

    private fun updateBrightLightLab(light: SensorData): SensorLab {
        val completed = light.x >= brightLuxThreshold
        return SensorLab(
            id = LabId.BRIGHT_LIGHT_DETECTION,
            title = "Bright-light detection",
            description = "Reach at least ${brightLuxThreshold.toInt()} lux",
            sourceSensor = SensorType.LIGHT,
            isCompleted = completed,
            progressText = "Current light: ${"%.1f".format(light.x)} lux"
        )
    }

    private fun updateProximityLab(proximity: SensorData): SensorLab {
        val completed = proximity.x <= proximityNearThreshold
        return SensorLab(
            id = LabId.PROXIMITY_TRIGGER,
            title = "Near-object detection",
            description = "Bring an object within ${proximityNearThreshold} cm",
            sourceSensor = SensorType.PROXIMITY,
            isCompleted = completed,
            progressText = "Current distance: ${"%.2f".format(proximity.x)} cm"
        )
    }

    private fun updatePressureShiftLab(pressure: SensorData): SensorLab {
        if (pressureBaseline == null) {
            pressureBaseline = pressure.x
        }
        val baseline = pressureBaseline ?: pressure.x
        val delta = abs(pressure.x - baseline)
        val completed = delta >= pressureShiftThreshold
        return SensorLab(
            id = LabId.PRESSURE_SHIFT,
            title = "Pressure shift challenge",
            description = "Change pressure by at least ${pressureShiftThreshold} hPa from baseline",
            sourceSensor = SensorType.PRESSURE,
            isCompleted = completed,
            progressText = "Baseline ${"%.1f".format(baseline)} hPa | current ${"%.1f".format(pressure.x)} hPa | delta ${"%.2f".format(delta)}"
        )
    }

    private fun updateAudioQuietLab(audio: SensorData): SensorLab {
        val completed = audio.x <= quietDbfsThreshold
        return SensorLab(
            id = LabId.AUDIO_QUIET_ZONE,
            title = "Quiet-zone detection",
            description = "Reduce audio level to <= ${quietDbfsThreshold.toInt()} dBFS",
            sourceSensor = SensorType.AUDIO_LEVEL,
            isCompleted = completed,
            progressText = "Current audio: ${"%.1f".format(audio.x)} dBFS"
        )
    }

    private fun updateAudioPeakLab(audio: SensorData): SensorLab {
        val completed = audio.x >= peakDbfsThreshold
        return SensorLab(
            id = LabId.AUDIO_PEAK_EVENT,
            title = "Audio peak event",
            description = "Reach audio level >= ${peakDbfsThreshold.toInt()} dBFS",
            sourceSensor = SensorType.AUDIO_LEVEL,
            isCompleted = completed,
            progressText = "Current audio: ${"%.1f".format(audio.x)} dBFS"
        )
    }

    private fun updateBluetoothProximityLab(bluetooth: SensorData): SensorLab {
        val completed = bluetooth.x >= bluetoothNearThreshold
        return SensorLab(
            id = LabId.BLUETOOTH_PROXIMITY,
            title = "Bluetooth proximity",
            description = "Reach BLE signal stronger than ${bluetoothNearThreshold.toInt()} dBm",
            sourceSensor = SensorType.BLUETOOTH_SIGNAL,
            isCompleted = completed,
            progressText = "Current BLE signal: ${"%.1f".format(bluetooth.x)} dBm"
        )
    }

    private fun updateCameraCapabilityLab(cameraMetadata: SensorData): SensorLab {
        val completed = cameraMetadata.x >= cameraCapabilityThreshold
        return SensorLab(
            id = LabId.CAMERA_CAPABILITY_CHECK,
            title = "Camera capability check",
            description = "Reach camera metadata score >= ${cameraCapabilityThreshold.toInt()}",
            sourceSensor = SensorType.CAMERA_METADATA,
            isCompleted = completed,
            progressText = "Current metadata score: ${"%.1f".format(cameraMetadata.x)}"
        )
    }
}
