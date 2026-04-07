package com.sensorscope.core.sensor

import android.Manifest
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.sensorscope.core.model.SamplingMode
import com.sensorscope.core.model.SensorData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@Singleton
class BluetoothSignalProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun isAvailable(): Boolean {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager ?: return false
        val adapter = manager.adapter ?: return false
        if (!adapter.isEnabled) return false
        return hasScanPermission()
    }

    fun observe(samplingMode: SamplingMode): Flow<SensorData> = callbackFlow {
        if (!isAvailable()) {
            close()
            return@callbackFlow
        }

        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        val adapter = manager?.adapter
        val scanner = adapter?.bluetoothLeScanner
        if (scanner == null) {
            close()
            return@callbackFlow
        }

        var smoothedRssi = -100f
        val alpha = when (samplingMode) {
            SamplingMode.NORMAL -> 0.18f
            SamplingMode.FAST -> 0.35f
        }

        val callback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                val rssi = result?.rssi?.toFloat() ?: return
                smoothedRssi = (1 - alpha) * smoothedRssi + alpha * rssi
                trySend(
                    SensorData(
                        x = smoothedRssi,
                        y = 0f,
                        z = 0f,
                        timestamp = System.nanoTime()
                    )
                )
            }
        }

        scanner.startScan(callback)

        awaitClose {
            scanner.stopScan(callback)
        }
    }

    private fun hasScanPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) ==
                PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
        }
    }
}
