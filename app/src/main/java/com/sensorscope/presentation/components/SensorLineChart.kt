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
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.sensorscope.core.model.AxisModel
import com.sensorscope.core.model.SensorData

private val COLOR_X = Color.parseColor("#4FD1C5")   // Cyan400
private val COLOR_Y = Color.parseColor("#9AE6B4")   // Mint300
private val COLOR_Z = Color.parseColor("#F6AD55")   // Amber
private val GRID    = Color.argb(40, 255, 255, 255)
private val AXIS_TXT = Color.argb(180, 226, 232, 240)  // Gray200 @ 70%

@Composable
fun SensorLineChart(
    points: List<SensorData>,
    axisModel: AxisModel,
    modifier: Modifier = Modifier,
    unit: String = ""
) {
    val renderedCount = remember { mutableStateOf(0) }
    val renderedFirstTs = remember { mutableStateOf<Long?>(null) }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        factory = { context ->
            LineChart(context).apply {
                // ── Chart-level settings ──────────────────────────────────
                description.isEnabled = false
                setBackgroundColor(Color.TRANSPARENT)
                setDrawGridBackground(false)
                setTouchEnabled(true)
                setPinchZoom(true)
                isAutoScaleMinMaxEnabled = true
                setNoDataText("Enable sensor to see data")
                setNoDataTextColor(AXIS_TXT)

                // ── Legend ────────────────────────────────────────────────
                legend.apply {
                    isEnabled = true
                    textColor = AXIS_TXT
                    textSize = 11f
                    form = Legend.LegendForm.LINE
                    formLineWidth = 2f
                    horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                    verticalAlignment = Legend.LegendVerticalAlignment.TOP
                    orientation = Legend.LegendOrientation.HORIZONTAL
                    setDrawInside(true)
                }

                // ── Left Y axis ───────────────────────────────────────────
                axisLeft.apply {
                    textColor = AXIS_TXT
                    textSize = 10f
                    gridColor = GRID
                    axisLineColor = GRID
                    setDrawGridLines(true)
                    setLabelCount(5, true)
                    if (unit.isNotEmpty()) {
                        valueFormatter = UnitValueFormatter(unit)
                    }
                }
                axisRight.isEnabled = false

                // ── X axis ────────────────────────────────────────────────
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    textColor = AXIS_TXT
                    textSize = 9f
                    gridColor = GRID
                    axisLineColor = GRID
                    setDrawGridLines(true)
                    setDrawLabels(false)   // sample index has no meaningful label
                    setLabelCount(0, false)
                }
            }
        },
        update = { chart ->
            if (points.isEmpty()) {
                chart.clear()
                renderedCount.value = 0
                renderedFirstTs.value = null
                chart.invalidate()
                return@AndroidView
            }

            val firstTs = points.first().timestamp
            val canAppend = chart.data != null &&
                renderedFirstTs.value == firstTs &&
                points.size >= renderedCount.value

            if (!canAppend) {
                // ── Full redraw ───────────────────────────────────────────
                val xEntries = ArrayList<Entry>(points.size)
                val yEntries = ArrayList<Entry>(points.size)
                val zEntries = ArrayList<Entry>(points.size)
                points.forEachIndexed { i, d ->
                    val idx = i.toFloat()
                    xEntries += Entry(idx, d.x)
                    yEntries += Entry(idx, d.y)
                    zEntries += Entry(idx, d.z)
                }

                val xSet = makeDataSet(xEntries, if (axisModel == AxisModel.TRI_AXIS) "X" else unit, COLOR_X)
                val dataSets = mutableListOf<ILineDataSet>(xSet)
                if (axisModel == AxisModel.TRI_AXIS) {
                    dataSets += makeDataSet(yEntries, "Y", COLOR_Y)
                    dataSets += makeDataSet(zEntries, "Z", COLOR_Z)
                }
                chart.data = LineData(dataSets)
            } else {
                // ── Incremental append ────────────────────────────────────
                val lineData = chart.data ?: return@AndroidView
                val xSet = lineData.getDataSetByIndex(0) as? LineDataSet ?: return@AndroidView
                val ySet = lineData.getDataSetByIndex(1) as? LineDataSet
                val zSet = lineData.getDataSetByIndex(2) as? LineDataSet

                for (i in renderedCount.value until points.size) {
                    val d = points[i]
                    val idx = i.toFloat()
                    xSet.addEntryOrdered(Entry(idx, d.x))
                    ySet?.addEntryOrdered(Entry(idx, d.y))
                    zSet?.addEntryOrdered(Entry(idx, d.z))
                }
                lineData.notifyDataChanged()
                chart.notifyDataSetChanged()
            }

            renderedCount.value = points.size
            renderedFirstTs.value = firstTs
            chart.moveViewToX(points.size.toFloat())
            chart.invalidate()
        }
    )
}

// ── Helpers ───────────────────────────────────────────────────────────────────

private fun makeDataSet(entries: List<Entry>, label: String, color: Int): LineDataSet =
    LineDataSet(entries, label).apply {
        this.color = color
        lineWidth = 2f
        mode = LineDataSet.Mode.CUBIC_BEZIER
        cubicIntensity = 0.15f
        setDrawCircles(false)
        setDrawValues(false)
        setDrawFilled(true)
        fillAlpha = 18
        fillColor = color
        highLightColor = color
    }

/** Appends the sensor unit to every Y-axis label. */
private class UnitValueFormatter(private val unit: String) :
    com.github.mikephil.charting.formatter.ValueFormatter() {
    override fun getFormattedValue(value: Float): String = "%.1f".format(value)
}
