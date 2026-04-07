package com.sensorscope.core.model

import org.junit.Assert.assertEquals
import org.junit.Test

class SensorTypeCapabilityTest {

    @Test
    fun threeAxisSensorsExposeExpectedMetadata() {
        assertEquals(AxisModel.TRI_AXIS, SensorType.ACCELEROMETER.capability.axisModel)
        assertEquals(ValueDisplayFormat.XYZ, SensorType.ACCELEROMETER.capability.displayFormat)
        assertEquals("m/s^2", SensorType.ACCELEROMETER.capability.unit)

        assertEquals(AxisModel.TRI_AXIS, SensorType.GYROSCOPE.capability.axisModel)
        assertEquals(ValueDisplayFormat.XYZ, SensorType.GYROSCOPE.capability.displayFormat)
        assertEquals("rad/s", SensorType.GYROSCOPE.capability.unit)

        assertEquals(AxisModel.TRI_AXIS, SensorType.MAGNETOMETER.capability.axisModel)
        assertEquals(ValueDisplayFormat.XYZ, SensorType.MAGNETOMETER.capability.displayFormat)
        assertEquals("uT", SensorType.MAGNETOMETER.capability.unit)
    }

    @Test
    fun singleAxisSensorsExposeExpectedMetadata() {
        assertEquals(AxisModel.SINGLE_VALUE, SensorType.LIGHT.capability.axisModel)
        assertEquals(ValueDisplayFormat.VALUE_WITH_UNIT, SensorType.LIGHT.capability.displayFormat)
        assertEquals("lux", SensorType.LIGHT.capability.unit)

        assertEquals(AxisModel.SINGLE_VALUE, SensorType.PROXIMITY.capability.axisModel)
        assertEquals(ValueDisplayFormat.VALUE_WITH_UNIT, SensorType.PROXIMITY.capability.displayFormat)
        assertEquals("cm", SensorType.PROXIMITY.capability.unit)

        assertEquals(AxisModel.SINGLE_VALUE, SensorType.PRESSURE.capability.axisModel)
        assertEquals(ValueDisplayFormat.VALUE_WITH_UNIT, SensorType.PRESSURE.capability.displayFormat)
        assertEquals("hPa", SensorType.PRESSURE.capability.unit)

        assertEquals(AxisModel.SINGLE_VALUE, SensorType.AUDIO_LEVEL.capability.axisModel)
        assertEquals(ValueDisplayFormat.VALUE_WITH_UNIT, SensorType.AUDIO_LEVEL.capability.displayFormat)
        assertEquals("dBFS", SensorType.AUDIO_LEVEL.capability.unit)

        assertEquals(AxisModel.SINGLE_VALUE, SensorType.CAMERA_METADATA.capability.axisModel)
        assertEquals(ValueDisplayFormat.VALUE_WITH_UNIT, SensorType.CAMERA_METADATA.capability.displayFormat)
        assertEquals("score", SensorType.CAMERA_METADATA.capability.unit)

        assertEquals(AxisModel.SINGLE_VALUE, SensorType.BLUETOOTH_SIGNAL.capability.axisModel)
        assertEquals(ValueDisplayFormat.VALUE_WITH_UNIT, SensorType.BLUETOOTH_SIGNAL.capability.displayFormat)
        assertEquals("dBm", SensorType.BLUETOOTH_SIGNAL.capability.unit)
    }
}