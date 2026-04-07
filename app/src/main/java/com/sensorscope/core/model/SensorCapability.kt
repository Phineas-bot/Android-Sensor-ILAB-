package com.sensorscope.core.model

enum class AxisModel {
    SINGLE_VALUE,
    TRI_AXIS
}

enum class ValueDisplayFormat {
    VALUE_WITH_UNIT,
    XYZ
}

data class SensorCapability(
    val unit: String,
    val axisModel: AxisModel,
    val displayFormat: ValueDisplayFormat
) {
    val axisCount: Int
        get() = when (axisModel) {
            AxisModel.SINGLE_VALUE -> 1
            AxisModel.TRI_AXIS -> 3
        }

    fun formatReading(data: SensorData): String {
        return when (displayFormat) {
            ValueDisplayFormat.VALUE_WITH_UNIT -> "Value: %.2f %s".format(data.x, unit)
            ValueDisplayFormat.XYZ -> "X: %.2f  Y: %.2f  Z: %.2f".format(data.x, data.y, data.z)
        }
    }
}