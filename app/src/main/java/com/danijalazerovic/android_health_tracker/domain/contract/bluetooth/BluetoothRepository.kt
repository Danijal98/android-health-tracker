package com.danijalazerovic.android_health_tracker.domain.contract.bluetooth

import com.danijalazerovic.android_health_tracker.domain.Resource
import com.danijalazerovic.android_health_tracker.domain.model.BluetoothDevice
import com.danijalazerovic.android_health_tracker.domain.model.HealthData
import kotlinx.coroutines.flow.Flow

interface BluetoothRepository {
    fun connectAndReadData(deviceAddress: String): Flow<HealthData>
    suspend fun saveData(healthData: HealthData): Resource<Unit>
    fun startScanning(): Flow<BluetoothDevice>
    fun stopScanning()
}