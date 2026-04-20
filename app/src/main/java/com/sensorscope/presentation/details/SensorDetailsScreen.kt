package com.sensorscope.presentation.details

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sensorscope.core.model.AxisModel
import com.sensorscope.core.model.SamplingMode
import com.sensorscope.core.model.SensorType
import com.sensorscope.presentation.components.SensorLineChart
import com.sensorscope.presentation.sensor.SensorCollectionScope
import com.sensorscope.presentation.sensor.SensorViewModel
import com.sensorscope.ui.theme.Cyan400
import com.sensorscope.ui.theme.Gray200
import com.sensorscope.ui.theme.Mint300
import com.sensorscope.ui.theme.Slate800

private val AXIS_Z_COLOR = Color(0xFFF6AD55)   // Amber

@Composable
fun SensorDetailsScreen(
    sensorType: SensorType,
    viewModel: SensorViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val latest  = state.latestValues[sensorType]
    val history = state.chartSeries[sensorType].orEmpty()
    val isEnabled = sensorType in state.enabledSensors

    // Detail scope auto-starts collection for this sensor immediately
    DisposableEffect(sensorType) {
        viewModel.setCollectionScope(SensorCollectionScope.Detail(sensorType))
        onDispose { }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // ── Title row ─────────────────────────────────────────────────────────
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        sensorType.displayName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Gray200
                    )
                    Text(
                        "Unit: ${sensorType.unit}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Cyan400.copy(alpha = 0.8f)
                    )
                }
                // Per-sensor start / stop
                SensorToggleButton(
                    isEnabled = isEnabled,
                    onToggle = { viewModel.toggleSensor(sensorType) }
                )

                state.trendSummaries[sensorType]?.let { trend ->
                    Text(
                        text = "Trend: ${trend.direction}  avg ${"%.2f".format(trend.average)}  min ${"%.2f".format(trend.minimum)}  max ${"%.2f".format(trend.maximum)}",
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        if (state.crossSensorInsights.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Cross-Sensor Insights")
                    state.crossSensorInsights.forEach { insight ->
                        Text("- $insight", modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }

        // ── Live values card ──────────────────────────────────────────────────
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Slate800),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Live Values",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Gray200
                        )
                        if (isEnabled && latest != null) {
                            LiveIndicator()
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    if (latest == null) {
                        Text(
                            "No data — tap Start to begin monitoring",
                            color = Gray200.copy(alpha = 0.35f),
                            fontSize = 13.sp
                        )
                    } else {
                        when (sensorType.capability.axisModel) {
                            AxisModel.TRI_AXIS -> {
                                AxisValueRow("X", latest.x, sensorType.unit, Cyan400)
                                Spacer(Modifier.height(6.dp))
                                AxisValueRow("Y", latest.y, sensorType.unit, Mint300)
                                Spacer(Modifier.height(6.dp))
                                AxisValueRow("Z", latest.z, sensorType.unit, AXIS_Z_COLOR)
                            }
                            AxisModel.SINGLE_VALUE -> {
                                AxisValueRow(sensorType.displayName, latest.x, sensorType.unit, Cyan400)
                            }
                        }
                    }
                }
            }
        }

        // ── Chart ─────────────────────────────────────────────────────────────
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Slate800),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "History  (last ${history.size} samples)",
                        fontSize = 12.sp,
                        color = Gray200.copy(alpha = 0.5f),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    SensorLineChart(
                        points = history,
                        axisModel = sensorType.capability.axisModel,
                        unit = sensorType.unit
                    )
                }
            }
        }

        // ── Sampling rate card ────────────────────────────────────────────────
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Slate800),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Fast Sampling",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Gray200
                        )
                        Text(
                            if (state.samplingMode == SamplingMode.FAST) "33 ms update rate" else "100 ms update rate",
                            fontSize = 12.sp,
                            color = Gray200.copy(alpha = 0.45f)
                        )
                    }
                    Switch(
                        checked = state.samplingMode == SamplingMode.FAST,
                        onCheckedChange = viewModel::setSamplingRateFast,
                        colors = SwitchDefaults.colors(checkedThumbColor = Cyan400, checkedTrackColor = Cyan400.copy(alpha = 0.4f))
                    )
                }
            }
        }

        item { Spacer(Modifier.height(8.dp)) }
    }
}

// ─── Composables ──────────────────────────────────────────────────────────────

@Composable
private fun SensorToggleButton(isEnabled: Boolean, onToggle: () -> Unit) {
    val btnColor = if (isEnabled) Color(0xFFFC8181) else Cyan400
    androidx.compose.material3.Surface(
        shape = RoundedCornerShape(12.dp),
        color = btnColor.copy(alpha = 0.14f),
        onClick = onToggle,
        modifier = Modifier.border(1.dp, btnColor.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
    ) {
        Text(
            text = if (isEnabled) "Stop" else "Start",
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = btnColor
        )
    }
}

@Composable
private fun AxisValueRow(label: String, value: Float, unit: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = "%.4f".format(value),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Gray200
            )
            Text(
                text = unit,
                fontSize = 11.sp,
                color = Gray200.copy(alpha = 0.45f)
            )
        }
    }
}

@Composable
private fun LiveIndicator() {
    val inf = rememberInfiniteTransition(label = "live")
    val alpha by inf.animateFloat(
        initialValue = 0.4f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(650), RepeatMode.Reverse),
        label = "liveAlpha"
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(Mint300.copy(alpha = alpha))
        )
        Text(
            "LIVE",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Mint300.copy(alpha = alpha),
            letterSpacing = 1.sp
        )
    }
}
