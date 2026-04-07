package com.sensorscope.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sensorscope.core.model.SamplingMode
import com.sensorscope.core.model.SensorType
import com.sensorscope.presentation.components.SensorLineChart
import com.sensorscope.presentation.sensor.SensorCollectionScope
import com.sensorscope.presentation.sensor.SensorViewModel

@Composable
fun SensorDetailsScreen(
    sensorType: SensorType,
    viewModel: SensorViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val latest = state.latestValues[sensorType]
    val history = state.chartSeries[sensorType].orEmpty()

    DisposableEffect(sensorType) {
        viewModel.setCollectionScope(SensorCollectionScope.Detail(sensorType))
        onDispose { }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(sensorType.displayName, style = MaterialTheme.typography.headlineSmall)

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Real-time values")
                Text(
                    text = latest?.let(sensorType.capability::formatReading) ?: "No data yet"
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

        SensorLineChart(points = history, axisModel = sensorType.capability.axisModel)

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(onClick = viewModel::startListening, enabled = !state.isListening) {
                Text("Start")
            }
            Button(onClick = viewModel::stopListening, enabled = state.isListening) {
                Text("Stop")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Fast sampling")
            Switch(
                checked = state.samplingMode == SamplingMode.FAST,
                onCheckedChange = viewModel::setSamplingRateFast
            )
        }

        Text(
            text = "Active mode: ${if (state.samplingMode == SamplingMode.FAST) "FAST" else "NORMAL"}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
