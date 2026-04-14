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
            ValueDisplayFormat.VALUE_WITH_UNIT -> "%.3f $unit".format(data.x)
            ValueDisplayFormat.XYZ -> "X: %.3f  Y: %.3f  Z: %.3f  $unit".format(data.x, data.y, data.z)
        }
    }
}