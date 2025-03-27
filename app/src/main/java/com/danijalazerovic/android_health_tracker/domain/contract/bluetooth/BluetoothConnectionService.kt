package com.danijalazerovic.android_health_tracker.domain.contract.bluetooth

import com.danijalazerovic.android_health_tracker.domain.model.HealthData
import kotlinx.coroutines.flow.Flow

interface BluetoothConnectionService {
    fun connectAndReadData(deviceAddress: String): Flow<HealthData>
}