package com.sensorscope.presentation.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import com.sensorscope.core.model.SensorData
import com.sensorscope.core.model.SensorType
import com.sensorscope.presentation.components.SensorLineChart
import com.sensorscope.presentation.sensor.SensorCollectionScope
import com.sensorscope.presentation.sensor.SensorViewModel
import com.sensorscope.ui.theme.Cyan400
import com.sensorscope.ui.theme.Gray200
import com.sensorscope.ui.theme.Mint300
import com.sensorscope.ui.theme.Slate800

private val sensorAccentColors = mapOf(
    SensorType.ACCELEROMETER to Color(0xFF4FD1C5),
    SensorType.GYROSCOPE      to Color(0xFF9AE6B4),
    SensorType.MAGNETOMETER   to Color(0xFFF6AD55),
    SensorType.LIGHT          to Color(0xFFFBD38D),
    SensorType.PROXIMITY      to Color(0xFFFC8181),
    SensorType.PRESSURE       to Color(0xFF90CDF4)
)

private val sensorEmojis = mapOf(
    SensorType.ACCELEROMETER to "⚡",
    SensorType.GYROSCOPE      to "🌀",
    SensorType.MAGNETOMETER   to "🧲",
    SensorType.LIGHT          to "💡",
    SensorType.PROXIMITY      to "📡",
    SensorType.PRESSURE       to "🌡️"
)

@Composable
fun DashboardScreen(
    viewModel: SensorViewModel,
    onOpenDetails: (SensorType) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        viewModel.setCollectionScope(SensorCollectionScope.Dashboard)
        onDispose { }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // ── Header ────────────────────────────────────────────────────────────
        DashboardHeader(
            isRecording = state.isRecording,
            onStartRecording = viewModel::startListening,
            onStopRecording = viewModel::stopListening
        )

        // ── Sensor cards ──────────────────────────────────────────────────────
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(SensorType.entries) { type ->
                val available = state.availableSensors[type] == true
                val enabled   = type in state.enabledSensors
                val current   = state.latestValues[type]
                val history   = state.chartSeries[type].orEmpty()
                val accent    = sensorAccentColors[type] ?: Cyan400

                SensorCard(
                    sensorType  = type,
                    accent      = accent,
                    isAvailable = available,
                    isEnabled   = enabled,
                    isRecording = state.isRecording,
                    currentData = current,
                    history     = history,
                    onToggle    = { viewModel.toggleSensor(type) },
                    onOpenDetails = { onOpenDetails(type) }
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

// ─── Header ───────────────────────────────────────────────────────────────────

@Composable
private fun DashboardHeader(
    isRecording: Boolean,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Dashboard",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Toggle individual sensors below",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }

                AnimatedContent(
                    targetState = isRecording,
                    transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(200)) },
                    label = "recordBtn"
                ) { recording ->
                    if (recording) {
                        Button(
                            onClick = onStopRecording,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFC8181)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            RecordingDot()
                            Spacer(Modifier.width(6.dp))
                            Text("Stop", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    } else {
                        OutlinedButton(
                            onClick = onStartRecording,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Cyan400)
                        ) {
                            Text("Record", color = Cyan400, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Recording status bar
            AnimatedVisibility(visible = isRecording) {
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    RecordingDot()
                    Text(
                        "Session recording in progress",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFFC8181)
                    )
                }
            }
        }
    }
}

@Composable
private fun RecordingDot() {
    val infinite = rememberInfiniteTransition(label = "dot")
    val alpha by infinite.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse),
        label = "dotAlpha"
    )
    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(Color(0xFFFC8181).copy(alpha = alpha))
    )
}

// ─── Sensor card ──────────────────────────────────────────────────────────────

@Composable
private fun SensorCard(
    sensorType: SensorType,
    accent: Color,
    isAvailable: Boolean,
    isEnabled: Boolean,
    isRecording: Boolean,
    currentData: SensorData?,
    history: List<SensorData>,
    onToggle: () -> Unit,
    onOpenDetails: () -> Unit
) {
    val cardAlpha = if (isAvailable) 1f else 0.45f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(cardAlpha)
            .clickable(enabled = isAvailable && isEnabled) { onOpenDetails() },
        colors = CardDefaults.cardColors(containerColor = Slate800),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // ── Card header row ───────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Accent circle with emoji
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(accent.copy(alpha = 0.15f))
                            .border(1.dp, accent.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(sensorEmojis[sensorType] ?: "📊", fontSize = 18.sp)
                    }

                    Column {
                        Text(
                            sensorType.displayName,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = Gray200
                        )
                        Text(
                            sensorType.unit,
                            fontSize = 11.sp,
                            color = accent.copy(alpha = 0.8f),
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Status badge
                    if (isEnabled && isRecording) {
                        Surface(
                            shape = RoundedCornerShape(100),
                            color = Color(0xFFFC8181).copy(alpha = 0.15f)
                        ) {
                            Text(
                                "● REC",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFC8181),
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    // Toggle button
                    if (!isAvailable) {
                        Surface(
                            shape = RoundedCornerShape(100),
                            color = Gray200.copy(alpha = 0.08f)
                        ) {
                            Text(
                                "N/A",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                fontSize = 11.sp,
                                color = Gray200.copy(alpha = 0.3f)
                            )
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(100),
                            color = if (isEnabled) accent.copy(alpha = 0.18f) else Gray200.copy(alpha = 0.06f),
                            modifier = Modifier
                                .clip(RoundedCornerShape(100))
                                .clickable { onToggle() }
                                .border(
                                    1.dp,
                                    if (isEnabled) accent.copy(alpha = 0.5f) else Gray200.copy(alpha = 0.15f),
                                    RoundedCornerShape(100)
                                )
                        ) {
                            Text(
                                text = if (isEnabled) "Stop" else "Start",
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isEnabled) accent else Gray200.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }

            // ── Live value ────────────────────────────────────────────────
            AnimatedVisibility(visible = isEnabled && currentData != null) {
                Column {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = currentData?.let { sensorType.capability.formatReading(it) } ?: "",
                        fontSize = 12.sp,
                        color = accent.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.3.sp
                    )
                }
            }

            // ── Mini chart ────────────────────────────────────────────────
            AnimatedVisibility(visible = isEnabled && history.isNotEmpty()) {
                Column {
                    Spacer(Modifier.height(8.dp))
                    SensorLineChart(
                        points = history,
                        axisModel = sensorType.capability.axisModel,
                        unit = sensorType.unit
                    )
                    if (isEnabled) {
                        Text(
                            "Tap card to open detailed view →",
                            fontSize = 11.sp,
                            color = Gray200.copy(alpha = 0.3f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // ── Idle hint ─────────────────────────────────────────────────
            AnimatedVisibility(visible = !isEnabled && isAvailable) {
                Text(
                    "Tap Start to begin monitoring",
                    fontSize = 12.sp,
                    color = Gray200.copy(alpha = 0.3f),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
