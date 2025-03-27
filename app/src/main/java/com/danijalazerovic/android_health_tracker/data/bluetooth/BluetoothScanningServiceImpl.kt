package com.danijalazerovic.android_health_tracker.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.ParcelUuid
import android.util.Log
import com.danijalazerovic.android_health_tracker.domain.contract.bluetooth.BluetoothScanningService
import com.danijalazerovic.android_health_tracker.domain.model.BluetoothConstants
import com.danijalazerovic.android_health_tracker.domain.model.BluetoothDevice
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onCompletion

@SuppressLint("MissingPermission")
class BluetoothScanningServiceImpl(context: Context) : BluetoothScanningService {

    private val bluetoothAdapter: BluetoothAdapter? =
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    private val scanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner
    private var scanCallback: ScanCallback? = null

    override fun startScanning(): Flow<BluetoothDevice> = callbackFlow {
        if (scanner == null) {
            close(IllegalStateException("Bluetooth LE Scanner is not available."))
            return@callbackFlow
        }

        val scanFilter = ScanFilter.Builder()
            .setServiceUuid(ParcelUuid(BluetoothConstants.HEALTH_MONITORING_SERVICE_UUID_STRING))
            .build()
        val scanSettings = ScanSettings.Builder().build()

        scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val device = result.device
                val bluetoothDevice = BluetoothDevice(
                    name = device.name ?: "Unknown Device",
                    address = device.address,
                    signalStrength = result.rssi
                )

                Log.d(
                    BluetoothScanningServiceImpl::class.simpleName,
                    "Device found: $bluetoothDevice"
                )

                trySend(bluetoothDevice).isSuccess
            }

            override fun onBatchScanResults(results: List<ScanResult>) {}

            override fun onScanFailed(errorCode: Int) {
                Log.e(
                    BluetoothScanningServiceImpl::class.simpleName,
                    "Scan failed with error code: $errorCode"
                )

                close(IllegalStateException("Scan failed with error code: \$errorCode"))
            }
        }

        scanner.startScan(listOf(scanFilter), scanSettings, scanCallback)

        awaitClose {
            scanner.stopScan(scanCallback)
        }
    }.distinctUntilChanged().onCompletion {
        stopScanning()
    }

    override fun stopScanning() {
        scanCallback?.let {
            scanner?.stopScan(it)
        }
        scanCallback = null
        Log.d(BluetoothScanningServiceImpl::class.simpleName, "Scanning stopped")
    }
}