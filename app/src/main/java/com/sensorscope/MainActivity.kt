package com.sensorscope

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.appcompat.app.AppCompatActivity
import com.sensorscope.core.model.SensorType
import com.sensorscope.presentation.dashboard.DashboardScreen
import com.sensorscope.presentation.details.SensorDetailsScreen
import com.sensorscope.presentation.labs.LabsScreen
import com.sensorscope.presentation.logs.LogsScreen
import com.sensorscope.presentation.logs.LogsViewModel
import com.sensorscope.presentation.navigation.Destination
import com.sensorscope.presentation.sensor.SensorViewModel
import com.sensorscope.ui.theme.SensorScopeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val storagePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    private val runtimePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestStoragePermissionIfRequired()
        requestSensorPermissionsIfRequired()

        setContent {
            SensorScopeTheme {
                SensorScopeApp(onShareUri = { uri -> shareFile(uri) })
            }
        }
    }

    private fun requestStoragePermissionIfRequired() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) return

        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val granted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        if (!granted) {
            storagePermissionLauncher.launch(permission)
        }
    }

    private fun requestSensorPermissionsIfRequired() {
        val required = mutableListOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            required += Manifest.permission.BLUETOOTH_SCAN
            required += Manifest.permission.BLUETOOTH_CONNECT
        } else {
            required += Manifest.permission.ACCESS_FINE_LOCATION
        }

        val missing = required.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missing.isNotEmpty()) {
            runtimePermissionsLauncher.launch(missing.toTypedArray())
        }
    }

    private fun shareFile(uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, "Share CSV"))
    }
}

@Composable
private fun SensorScopeApp(onShareUri: (Uri) -> Unit) {
    val navController = rememberNavController()
    val sensorViewModel: SensorViewModel = hiltViewModel()
    val logsViewModel: LogsViewModel = hiltViewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val exportedUri by logsViewModel.exportedUri.collectAsStateWithLifecycle()

    LaunchedEffect(exportedUri) {
        val uri = exportedUri?.let(Uri::parse) ?: return@LaunchedEffect
        onShareUri(uri)
        logsViewModel.clearExportState()
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf(
                    Destination.Dashboard to "Dashboard",
                    Destination.Labs to "Labs",
                    Destination.Logs to "Logs"
                ).forEach { (destination, label) ->
                    val selected = currentRoute == destination.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(Destination.Dashboard.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = { Text(label) },
                        icon = { Text(label.take(1)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Destination.Dashboard.route) {
                DashboardScreen(
                    viewModel = sensorViewModel,
                    onOpenDetails = { sensorType ->
                        navController.navigate(Destination.SensorDetail.createRoute(sensorType))
                    }
                )
            }
            composable(Destination.Labs.route) {
                LabsScreen(viewModel = sensorViewModel)
            }
            composable(Destination.Logs.route) {
                LogsScreen(viewModel = logsViewModel)
            }
            composable(
                route = Destination.SensorDetail.route,
                arguments = listOf(navArgument("sensorType") { type = NavType.StringType })
            ) { backStackEntry ->
                val sensorTypeName = backStackEntry.arguments?.getString("sensorType")
                val sensorType = runCatching { SensorType.valueOf(sensorTypeName ?: "") }.getOrDefault(SensorType.ACCELEROMETER)
                SensorDetailsScreen(
                    sensorType = sensorType,
                    viewModel = sensorViewModel
                )
            }
        }
    }
}
