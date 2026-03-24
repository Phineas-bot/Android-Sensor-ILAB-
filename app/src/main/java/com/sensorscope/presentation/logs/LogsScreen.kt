package com.sensorscope.presentation.logs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sensorscope.core.security.BiometricAuthenticator
import java.text.DateFormat
import java.util.Date

@Composable
fun LogsScreen(viewModel: LogsViewModel) {
    val sessions by viewModel.sessions.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as? FragmentActivity
    val formatter = remember { DateFormat.getDateTimeInstance() }
    var unlocked by remember { mutableStateOf(false) }
    var authMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        if (!unlocked) {
            Text("Logs are protected", style = MaterialTheme.typography.headlineSmall)
            Text("Authenticate to view and export sessions", modifier = Modifier.padding(top = 8.dp))
            Button(
                onClick = {
                    val hostActivity = activity ?: run {
                        authMessage = "Biometric authentication unavailable"
                        return@Button
                    }
                    val authenticator = BiometricAuthenticator(hostActivity)
                    if (!authenticator.canAuthenticate()) {
                        authMessage = "Biometric hardware not available"
                        return@Button
                    }
                    authenticator.authenticate(
                        title = "Unlock Sensor Logs",
                        subtitle = "Use biometrics to continue",
                        onSuccess = { unlocked = true },
                        onFailure = { authMessage = it }
                    )
                },
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Text("Authenticate")
            }
            authMessage?.let {
                Text(it, modifier = Modifier.padding(top = 8.dp), color = MaterialTheme.colorScheme.secondary)
            }
            return
        }

        Text("Recorded Sessions", style = MaterialTheme.typography.headlineSmall)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sessions) { session ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(session.name, fontWeight = FontWeight.Bold)
                        Text("Start: ${formatter.format(Date(session.startedAtMillis))}")
                        Text(
                            text = "End: ${session.endedAtMillis?.let { formatter.format(Date(it)) } ?: "In progress"}",
                            modifier = Modifier.padding(top = 2.dp)
                        )

                        Row(modifier = Modifier.padding(top = 10.dp)) {
                            Button(onClick = { viewModel.exportSession(session.id) }) {
                                Text("Export CSV")
                            }
                        }
                    }
                }
            }
        }
    }
}
