package com.sensorscope.presentation.logs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sensorscope.domain.model.SensorSessionSummary
import com.sensorscope.ui.theme.Cyan400
import com.sensorscope.ui.theme.Gray200
import com.sensorscope.ui.theme.Mint300
import com.sensorscope.ui.theme.Slate800
import java.text.DateFormat
import java.util.Date

@Composable
fun LogsScreen(viewModel: LogsViewModel) {
    val sessions by viewModel.sessions.collectAsStateWithLifecycle()
    val exportMessage by viewModel.exportMessage.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as? FragmentActivity
    val authHandler = remember(activity) { logsAuthHandler(activity) }
    val formatter = remember { DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT) }
    var unlocked by remember { mutableStateOf(false) }
    var authMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // ── Header ────────────────────────────────────────────────────────────
        Surface(color = MaterialTheme.colorScheme.surface, tonalElevation = 2.dp) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    "Session Logs",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Gray200
                )
                Text(
                    "Tap Authenticate to unlock your recordings",
                    style = MaterialTheme.typography.bodySmall,
                    color = Gray200.copy(alpha = 0.45f),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        if (!unlocked) {
            // ── Locked state ──────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("🔐", fontSize = 56.sp)
                Spacer(Modifier.height(20.dp))
                Text(
                    "Logs are biometric-protected",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Gray200
                )
                Text(
                    "Authenticate with fingerprint or face ID to view and export your sensor sessions.",
                    color = Gray200.copy(alpha = 0.5f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp),
                    lineHeight = 20.sp
                )
                Spacer(Modifier.height(28.dp))
                Button(
                    onClick = {
                        authHandler.authenticate(
                            onSuccess = { unlocked = true },
                            onMessage = { authMessage = it }
                        )
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Cyan400)
                ) {
                    Text(
                        "Authenticate",
                        color = Color(0xFF0B1220),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                authMessage?.let { msg ->
                    Text(
                        msg,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
            return
        }

        // ── Export status banner ──────────────────────────────────────────────
        exportMessage?.let { message ->
            Surface(color = Cyan400.copy(alpha = 0.12f)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        message,
                        color = Cyan400,
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedButton(
                        onClick = viewModel::clearExportMessage,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Cyan400.copy(alpha = 0.5f)),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("Dismiss", color = Cyan400, fontSize = 12.sp)
                    }
                }
            }
        }

        // ── Session list ──────────────────────────────────────────────────────
        if (sessions.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("📋", fontSize = 48.sp)
                Spacer(Modifier.height(16.dp))
                Text(
                    "No sessions yet",
                    fontWeight = FontWeight.SemiBold,
                    color = Gray200,
                    fontSize = 16.sp
                )
                Text(
                    "Start a recording session on the Dashboard to capture sensor data.",
                    color = Gray200.copy(alpha = 0.4f),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 8.dp),
                    lineHeight = 19.sp
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        "${sessions.size} session${if (sessions.size != 1) "s" else ""}",
                        fontSize = 12.sp,
                        color = Gray200.copy(alpha = 0.35f),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                items(sessions, key = { it.id }) { session ->
                    SessionCard(
                        session = session,
                        dateFormatter = formatter,
                        onExport = { viewModel.exportSession(session.id) }
                    )
                }
                item { Spacer(Modifier.height(8.dp)) }
            }
        }
    }
}

// ─── Session card ─────────────────────────────────────────────────────────────

@Composable
private fun SessionCard(
    session: SensorSessionSummary,
    dateFormatter: DateFormat,
    onExport: () -> Unit
) {
    val isInProgress = session.endedAtMillis == null
    val statusColor  = if (isInProgress) Color(0xFFFC8181) else Mint300

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Slate800,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isInProgress) Color(0xFFFC8181).copy(alpha = 0.4f) else Color.Transparent,
                RoundedCornerShape(16.dp)
            )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // ── Name + status ─────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    session.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Gray200,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    shape = RoundedCornerShape(100),
                    color = statusColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = if (isInProgress) "● In Progress" else "✓ Complete",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor,
                        letterSpacing = 0.3.sp
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
            HorizontalDivider(color = Gray200.copy(alpha = 0.06f))
            Spacer(Modifier.height(10.dp))

            // ── Metadata grid ─────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MetaItem(
                    label = "Started",
                    value = dateFormatter.format(Date(session.startedAtMillis)),
                    modifier = Modifier.weight(1f)
                )
                MetaItem(
                    label = if (isInProgress) "Ended" else "Ended",
                    value = session.endedAtMillis
                        ?.let { dateFormatter.format(Date(it)) }
                        ?: "—",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MetaItem(
                    label = "Duration",
                    value = session.durationSeconds?.formatDuration() ?: "—",
                    modifier = Modifier.weight(1f)
                )
                MetaItem(
                    label = "Readings",
                    value = "%,d".format(session.readingCount),
                    modifier = Modifier.weight(1f),
                    valueColor = Cyan400
                )
            }

            Spacer(Modifier.height(14.dp))

            // ── Export button ─────────────────────────────────────────────
            Button(
                onClick = onExport,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Cyan400.copy(alpha = 0.15f),
                    contentColor = Cyan400
                ),
                enabled = !isInProgress
            ) {
                Text(
                    if (isInProgress) "Session in progress…" else "Export as CSV",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun MetaItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = Gray200
) {
    Column(modifier = modifier) {
        Text(
            label.uppercase(),
            fontSize = 10.sp,
            color = Gray200.copy(alpha = 0.35f),
            letterSpacing = 1.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            value,
            fontSize = 13.sp,
            color = valueColor,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

private fun Long.formatDuration(): String {
    val h = this / 3600
    val m = (this % 3600) / 60
    val s = this % 60
    return when {
        h > 0 -> "${h}h ${m}m ${s}s"
        m > 0 -> "${m}m ${s}s"
        else  -> "${s}s"
    }
}
