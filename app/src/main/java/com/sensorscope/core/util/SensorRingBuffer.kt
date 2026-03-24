package com.sensorscope.core.util

import com.sensorscope.core.model.SensorData

class SensorRingBuffer(private val capacity: Int) {
    private val xValues = FloatArray(capacity)
    private val yValues = FloatArray(capacity)
    private val zValues = FloatArray(capacity)
    private val timestamps = LongArray(capacity)

    private var count = 0
    private var start = 0

    fun add(data: SensorData) {
        val index = (start + count) % capacity
        xValues[index] = data.x
        yValues[index] = data.y
        zValues[index] = data.z
        timestamps[index] = data.timestamp

        if (count < capacity) {
            count++
        } else {
            start = (start + 1) % capacity
        }
    }

    fun toList(): List<SensorData> {
        val result = ArrayList<SensorData>(count)
        repeat(count) { i ->
            val index = (start + i) % capacity
            result.add(
                SensorData(
                    x = xValues[index],
                    y = yValues[index],
                    z = zValues[index],
                    timestamp = timestamps[index]
                )
            )
        }
        return result
    }

    fun size(): Int = count
}
