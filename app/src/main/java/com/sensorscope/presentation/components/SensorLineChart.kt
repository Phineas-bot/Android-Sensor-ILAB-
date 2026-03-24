package com.sensorscope.presentation.components

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.sensorscope.core.model.SensorData

@Composable
fun SensorLineChart(
    points: List<SensorData>,
    modifier: Modifier = Modifier
) {
    val xEntries = remember { mutableStateListOf<Entry>() }
    val yEntries = remember { mutableStateListOf<Entry>() }
    val zEntries = remember { mutableStateListOf<Entry>() }

    LaunchedEffect(points) {
        xEntries.clear()
        yEntries.clear()
        zEntries.clear()

        points.forEachIndexed { index, sensorData ->
            val x = index.toFloat()
            xEntries += Entry(x, sensorData.x)
            yEntries += Entry(x, sensorData.y)
            zEntries += Entry(x, sensorData.z)
        }
    }

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
                setNoDataText("No sensor samples yet")
                setNoDataTextColor(Color.LTGRAY)
            }
        },
        update = { chart ->
            val xSet = LineDataSet(xEntries, "X").apply {
                color = Color.parseColor("#4FD1C5")
                setDrawCircles(false)
                lineWidth = 2f
                setDrawValues(false)
            }
            val ySet = LineDataSet(yEntries, "Y").apply {
                color = Color.parseColor("#9AE6B4")
                setDrawCircles(false)
                lineWidth = 2f
                setDrawValues(false)
            }
            val zSet = LineDataSet(zEntries, "Z").apply {
                color = Color.parseColor("#F6AD55")
                setDrawCircles(false)
                lineWidth = 2f
                setDrawValues(false)
            }

            chart.data = LineData(xSet, ySet, zSet)
            chart.invalidate()
        }
    )
}
