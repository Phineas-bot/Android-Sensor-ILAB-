package com.sensorscope

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sensorscope.core.model.SensorData
import com.sensorscope.core.util.SensorRingBuffer
import com.sensorscope.presentation.components.ChartIncrementalDiffEngine
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselinePerformanceBenchmarkTest {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @Test
    fun ringBuffer_addAndSnapshot() {
        benchmarkRule.measureRepeated {
            val buffer = SensorRingBuffer(capacity = 180)
            repeat(300) { index ->
                buffer.add(
                    SensorData(
                        x = index.toFloat(),
                        y = (index + 1).toFloat(),
                        z = (index + 2).toFloat(),
                        timestamp = index.toLong()
                    )
                )
            }
            buffer.toList()
        }
    }

    @Test
    fun chartDiffEngine_incrementalDelta() {
        benchmarkRule.measureRepeated {
            val engine = ChartIncrementalDiffEngine()
            val firstFrame = List(180) { i ->
                SensorData(i.toFloat(), i.toFloat(), i.toFloat(), i.toLong())
            }
            engine.nextDelta(firstFrame)

            val secondFrame = List(180) { i ->
                val timestamp = (i + 1).toLong()
                SensorData(timestamp.toFloat(), timestamp.toFloat(), timestamp.toFloat(), timestamp)
            }
            engine.nextDelta(secondFrame)
        }
    }
}
