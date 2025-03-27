package com.danijalazerovic.android_health_tracker.ui.viewmodel.state.bluetooth

import com.danijalazerovic.android_health_tracker.domain.model.BluetoothDevice

data class BluetoothScanningState(
    val isScanning: Boolean = false,
    val error: BluetoothScanningError? = null,
    val scannedDevices: List<BluetoothDevice> = emptyList(),
)

sealed class BluetoothScanningError : Exception() {
    data object ScanningError : BluetoothScanningError()
    data object DefaultError : BluetoothScanningError()
}