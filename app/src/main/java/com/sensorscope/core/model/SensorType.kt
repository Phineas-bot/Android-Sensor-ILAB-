package com.sensorscope.core.model

enum class SensorType(
    val displayName: String,
    val capability: SensorCapability
) {
    ACCELEROMETER(
        "Accelerometer",
        SensorCapability("m/s^2", AxisModel.TRI_AXIS, ValueDisplayFormat.XYZ)
    ),
    GYROSCOPE(
        "Gyroscope",
        SensorCapability("rad/s", AxisModel.TRI_AXIS, ValueDisplayFormat.XYZ)
    ),
    MAGNETOMETER(
        "Magnetometer",
        SensorCapability("uT", AxisModel.TRI_AXIS, ValueDisplayFormat.XYZ)
    ),
    LIGHT(
        "Light",
        SensorCapability("lux", AxisModel.SINGLE_VALUE, ValueDisplayFormat.VALUE_WITH_UNIT)
    ),
    PROXIMITY(
        "Proximity",
        SensorCapability("cm", AxisModel.SINGLE_VALUE, ValueDisplayFormat.VALUE_WITH_UNIT)
    ),
    PRESSURE(
        "Pressure",
        SensorCapability("hPa", AxisModel.SINGLE_VALUE, ValueDisplayFormat.VALUE_WITH_UNIT)
    );

    val unit: String
        get() = capability.unit

    val axisCount: Int
        get() = capability.axisCount
}
