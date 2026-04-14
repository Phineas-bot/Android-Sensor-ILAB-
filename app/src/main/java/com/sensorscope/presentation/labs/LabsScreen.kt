package com.sensorscope.presentation.labs

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sensorscope.domain.lab.SensorLab
import com.sensorscope.presentation.sensor.SensorCollectionScope
import com.sensorscope.presentation.sensor.SensorViewModel
import com.sensorscope.ui.theme.Cyan400
import com.sensorscope.ui.theme.Gray200
import com.sensorscope.ui.theme.Mint300
import com.sensorscope.ui.theme.Slate800

@Composable
fun LabsScreen(viewModel: SensorViewModel) {
    val labs by viewModel.labs.collectAsStateWithLifecycle()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Labs scope auto-enables all required sensors as soon as the screen is shown.
    DisposableEffect(Unit) {
        viewModel.setCollectionScope(SensorCollectionScope.Labs)
        onDispose { }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // ── Header ────────────────────────────────────────────────────────────
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    "Sensor Labs",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Gray200
                )
                Text(
                    "Sensors activate automatically on this screen",
                    style = MaterialTheme.typography.bodySmall,
                    color = Gray200.copy(alpha = 0.45f),
                    modifier = Modifier.padding(top = 2.dp)
                )

                // Required sensors chips
                Row(
                    modifier = Modifier.padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("⚡ Accel", "🧲 Magnet", "🌀 Gyro", "💡 Light").forEach { label ->
                        Surface(
                            shape = RoundedCornerShape(100),
                            color = Cyan400.copy(alpha = 0.10f)
                        ) {
                            Text(
                                label,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                color = Cyan400.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        if (labs.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🔬", fontSize = 48.sp)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Labs initialising…",
                        color = Gray200.copy(alpha = 0.4f),
                        fontSize = 15.sp
                    )
                    Text(
                        "Move the device to generate sensor data",
                        color = Gray200.copy(alpha = 0.25f),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(labs, key = { it.id }) { lab ->
                    LabCard(lab = lab)
                }
                item { Spacer(Modifier.height(8.dp)) }
            }
        }
    }
}

// ─── Lab card ─────────────────────────────────────────────────────────────────

@Composable
private fun LabCard(lab: SensorLab) {
    val accentColor = if (lab.isCompleted) Mint300 else Cyan400
    val progressAnim by animateFloatAsState(
        targetValue = lab.progress,
        animationSpec = tween(durationMillis = 300),
        label = "progress_${lab.id}"
    )
    val cardBorderColor by animateColorAsState(
        targetValue = if (lab.isCompleted) Mint300.copy(alpha = 0.5f) else Color.Transparent,
        animationSpec = tween(400),
        label = "border_${lab.id}"
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Slate800,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, cardBorderColor, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // ── Header row ────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Emoji icon circle
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.12f))
                            .border(1.dp, accentColor.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(lab.emoji, fontSize = 22.sp)
                    }

                    Column {
                        Text(
                            lab.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Gray200
                        )
                        Text(
                            lab.description,
                            fontSize = 12.sp,
                            color = Gray200.copy(alpha = 0.50f),
                            lineHeight = 17.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                // Completed badge
                if (lab.isCompleted) {
                    Surface(
                        shape = RoundedCornerShape(100),
                        color = Mint300.copy(alpha = 0.15f)
                    ) {
                        Text(
                            "✓ Done",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Mint300
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // ── Progress bar ─────────────────────────────────────────────
            LinearProgressIndicator(
                progress = { progressAnim },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = accentColor,
                trackColor = accentColor.copy(alpha = 0.12f),
                strokeCap = StrokeCap.Round
            )

            Spacer(Modifier.height(8.dp))

            // ── Progress text ─────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    lab.progressText,
                    fontSize = 12.sp,
                    color = accentColor.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "${(progressAnim * 100).toInt()}%",
                    fontSize = 12.sp,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
