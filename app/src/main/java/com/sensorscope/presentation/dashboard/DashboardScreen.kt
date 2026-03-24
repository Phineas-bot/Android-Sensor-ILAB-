package com.sensorscope.presentation.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sensorscope.core.model.SensorType
import com.sensorscope.presentation.components.SensorLineChart
import com.sensorscope.presentation.sensor.SensorCollectionScope
import com.sensorscope.presentation.sensor.SensorViewModel

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
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = viewModel::startListening, enabled = !state.isListening) {
                Text("Start")
            }
            Button(onClick = viewModel::stopListening, enabled = state.isListening) {
                Text("Stop")
            }
        }

        AnimatedVisibility(visible = state.errorMessage != null) {
            Text(
                text = state.errorMessage.orEmpty(),
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(SensorType.entries) { type ->
                val available = state.availableSensors[type] == true
                val current = state.latestValues[type]
                val history = state.chartSeries[type].orEmpty()

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = available) { onOpenDetails(type) },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(type.displayName, fontWeight = FontWeight.Bold)
                        Text(if (available) "Available" else "Not available")

                        if (current != null) {
                            Text(
                                text = "X: %.2f  Y: %.2f  Z: %.2f".format(current.x, current.y, current.z),
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }

                        if (history.isNotEmpty()) {
                            SensorLineChart(points = history, modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                }
            }
        }
    }
}
