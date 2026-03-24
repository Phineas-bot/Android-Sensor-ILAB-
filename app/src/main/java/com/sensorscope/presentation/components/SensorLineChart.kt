package com.sensorscope.presentation.components

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.sensorscope.core.model.SensorData

internal class ChartDelta(
    val appended: List<SensorData>,
    val maxWindowSize: Int,
    val reset: Boolean
)

internal class ChartIncrementalDiffEngine {
    private var lastTimestamp: Long? = null

    fun nextDelta(points: List<SensorData>): ChartDelta {
        if (points.isEmpty()) {
            lastTimestamp = null
            return ChartDelta(emptyList(), 0, true)
        }

        val previousTimestamp = lastTimestamp
        if (previousTimestamp == null) {
            lastTimestamp = points.last().timestamp
            return ChartDelta(points, points.size, true)
        }

        val previousIndex = points.indexOfLast { it.timestamp == previousTimestamp }
        return if (previousIndex == -1) {
            lastTimestamp = points.last().timestamp
            ChartDelta(points, points.size, true)
        } else {
            val appended = if (previousIndex + 1 < points.size) {
                points.subList(previousIndex + 1, points.size)
            } else {
                emptyList()
            }
            lastTimestamp = points.last().timestamp
            ChartDelta(appended, points.size, false)
        }
    }
}

private class ChartRenderState {
    var xCursor = 0f
    val diffEngine = ChartIncrementalDiffEngine()
}

private data class AxisSets(
    val xSet: LineDataSet,
    val ySet: LineDataSet,
    val zSet: LineDataSet
)

private fun createAxisSets(): AxisSets {
    val xSet = LineDataSet(mutableListOf(), "X").apply {
        color = Color.parseColor("#4FD1C5")
        setDrawCircles(false)
        lineWidth = 2f
        setDrawValues(false)
    }
    val ySet = LineDataSet(mutableListOf(), "Y").apply {
        color = Color.parseColor("#9AE6B4")
        setDrawCircles(false)
        lineWidth = 2f
        setDrawValues(false)
    }
    val zSet = LineDataSet(mutableListOf(), "Z").apply {
        color = Color.parseColor("#F6AD55")
        setDrawCircles(false)
        lineWidth = 2f
        setDrawValues(false)
    }
    return AxisSets(xSet, ySet, zSet)
}

private fun appendPoint(dataSet: LineDataSet, x: Float, y: Float) {
    dataSet.addEntry(Entry(x, y))
}

private fun trimToWindow(dataSet: LineDataSet, maxWindowSize: Int) {
    while (dataSet.entryCount > maxWindowSize) {
        dataSet.removeFirst()
    }
}

@Composable
fun SensorLineChart(
    points: List<SensorData>,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        factory = { context ->
            val renderState = ChartRenderState()
            val axisSets = createAxisSets()
            LineChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = true
                setTouchEnabled(true)
                setPinchZoom(true)
                axisRight.isEnabled = false
                axisLeft.textColor = Color.LTGRAY
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.textColor = Color.LTGRAY
                setNoDataText("No sensor samples yet")
                setNoDataTextColor(Color.LTGRAY)

                data = LineData(axisSets.xSet, axisSets.ySet, axisSets.zSet)
                tag = Pair(renderState, axisSets)
            }
        },
        update = { chart ->
            val (renderState, axisSets) = chart.tag as Pair<ChartRenderState, AxisSets>
            val delta = renderState.diffEngine.nextDelta(points)

            if (delta.reset) {
                axisSets.xSet.clear()
                axisSets.ySet.clear()
                axisSets.zSet.clear()
                renderState.xCursor = 0f
            }

            delta.appended.forEach { data ->
                val x = renderState.xCursor++
                appendPoint(axisSets.xSet, x, data.x)
                appendPoint(axisSets.ySet, x, data.y)
                appendPoint(axisSets.zSet, x, data.z)
            }

            trimToWindow(axisSets.xSet, delta.maxWindowSize)
            trimToWindow(axisSets.ySet, delta.maxWindowSize)
            trimToWindow(axisSets.zSet, delta.maxWindowSize)

            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
            chart.invalidate()
        }
    )
}
