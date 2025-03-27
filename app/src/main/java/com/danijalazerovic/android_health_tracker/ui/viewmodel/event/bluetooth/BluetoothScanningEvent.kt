package com.danijalazerovic.android_health_tracker.ui.viewmodel.event.bluetooth

sealed class BluetoothScanningEvent {
    data object StartScanning : BluetoothScanningEvent()
    data object StopScanning : BluetoothScanningEvent()
}