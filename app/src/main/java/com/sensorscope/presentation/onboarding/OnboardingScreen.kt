package com.sensorscope.presentation.onboarding

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingScreen(onContinue: () -> Unit) {
    var reveal by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { reveal = true }
    val alpha by animateFloatAsState(
        targetValue = if (reveal) 1f else 0f,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label = "onboardingAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFF5F7FF),
                        Color(0xFFEAF6EF),
                        Color(0xFFFDF6E9)
                    )
                )
            )
            .padding(20.dp)
            .alpha(alpha)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "SensorScope",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Professional mobile sensor intelligence, organized for labs and field validation.",
                    style = MaterialTheme.typography.bodyLarge
                )

                FeatureCard(
                    title = "Live monitoring",
                    detail = "Track motion, environment, audio, camera metadata, and Bluetooth signal in one dashboard."
                )
                FeatureCard(
                    title = "Insight-ready analytics",
                    detail = "See trend summaries and cross-sensor highlights instantly while data streams in."
                )
                FeatureCard(
                    title = "Lab workflows",
                    detail = "Run guided sensor experiments, keep completion state, and relaunch labs when needed."
                )
            }

            Button(
                onClick = onContinue,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Text(text = "Start Monitoring")
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(92.dp)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0x33A6D4FA), Color.Transparent)
                    )
                )
        )
    }
}

@Composable
private fun FeatureCard(title: String, detail: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.86f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(text = detail, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
