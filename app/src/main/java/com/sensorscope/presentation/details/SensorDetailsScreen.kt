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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    var fastSampling by remember { mutableStateOf(false) }

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
                    text = if (latest == null) "No data yet" else "X: %.2f  Y: %.2f  Z: %.2f".format(latest.x, latest.y, latest.z)
                )
            }
        }

        SensorLineChart(points = history)

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
                checked = fastSampling,
                onCheckedChange = {
                    fastSampling = it
                    viewModel.setSamplingRateFast(it)
                }
            )
        }
    }
}
