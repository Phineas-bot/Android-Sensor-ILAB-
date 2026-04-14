package com.sensorscope.presentation.components

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.sensorscope.core.model.AxisModel
import com.sensorscope.core.model.SensorData

@Composable
fun SensorLineChart(
    points: List<SensorData>,
    axisModel: AxisModel,
    modifier: Modifier = Modifier
) {
    val renderedCount = remember { mutableStateOf(0) }
    val renderedFirstTimestamp = remember { mutableStateOf<Long?>(null) }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = true
                setTouchEnabled(true)
                setPinchZoom(true)
                axisRight.isEnabled = false
                axisLeft.textColor = Color.LTGRAY
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.textColor = Color.LTGRAY
                isAutoScaleMinMaxEnabled = true
                setNoDataText("No sensor samples yet")
                setNoDataTextColor(Color.LTGRAY)
            }
        },
        update = { chart ->
            if (points.isEmpty()) {
                chart.clear()
                renderedCount.value = 0
                renderedFirstTimestamp.value = null
                chart.invalidate()
                return@AndroidView
            }

            val firstTimestamp = points.first().timestamp
            val canAppend =
                chart.data != null &&
                    renderedFirstTimestamp.value == firstTimestamp &&
                    points.size >= renderedCount.value

            if (!canAppend) {
                val xEntries = ArrayList<Entry>(points.size)
                val yEntries = ArrayList<Entry>(points.size)
                val zEntries = ArrayList<Entry>(points.size)
                points.forEachIndexed { index, sensorData ->
                    val x = index.toFloat()
                    xEntries += Entry(x, sensorData.x)
                    yEntries += Entry(x, sensorData.y)
                    zEntries += Entry(x, sensorData.z)
                }

                val xSet = LineDataSet(xEntries, "X").apply {
                    color = Color.parseColor("#4FD1C5")
                    setDrawCircles(false)
                    lineWidth = 2f
                    setDrawValues(false)
                }
                val dataSets = mutableListOf<ILineDataSet>(xSet)
                if (axisModel == AxisModel.TRI_AXIS) {
                    dataSets += LineDataSet(yEntries, "Y").apply {
                        color = Color.parseColor("#9AE6B4")
                        setDrawCircles(false)
                        lineWidth = 2f
                        setDrawValues(false)
                    }
                    dataSets += LineDataSet(zEntries, "Z").apply {
                        color = Color.parseColor("#F6AD55")
                        setDrawCircles(false)
                        lineWidth = 2f
                        setDrawValues(false)
                    }
                }

                chart.data = LineData(dataSets)
            } else {
                val lineData = chart.data ?: return@AndroidView
                val xSet = lineData.getDataSetByIndex(0) as? LineDataSet ?: return@AndroidView
                val ySet = lineData.getDataSetByIndex(1) as? LineDataSet
                val zSet = lineData.getDataSetByIndex(2) as? LineDataSet

                for (index in renderedCount.value until points.size) {
                    val sensorData = points[index]
                    val x = index.toFloat()
                    xSet.addEntryOrdered(Entry(x, sensorData.x))
                    ySet?.addEntryOrdered(Entry(x, sensorData.y))
                    zSet?.addEntryOrdered(Entry(x, sensorData.z))
                }

                lineData.notifyDataChanged()
                chart.notifyDataSetChanged()
            }

            renderedCount.value = points.size
            renderedFirstTimestamp.value = firstTimestamp
            chart.invalidate()
        }
    )
}
