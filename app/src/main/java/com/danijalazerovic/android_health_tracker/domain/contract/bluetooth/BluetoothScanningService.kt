package com.danijalazerovic.android_health_tracker.domain.contract.bluetooth

import com.danijalazerovic.android_health_tracker.domain.model.BluetoothDevice
import kotlinx.coroutines.flow.Flow

interface BluetoothScanningService {
    fun startScanning(): Flow<BluetoothDevice>
    fun stopScanning()
}