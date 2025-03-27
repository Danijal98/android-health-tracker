package com.danijalazerovic.android_health_tracker.data.bluetooth

import android.util.Log
import com.danijalazerovic.android_health_tracker.domain.contract.bluetooth.BluetoothConnectionService
import com.danijalazerovic.android_health_tracker.domain.contract.bluetooth.BluetoothRepository
import com.danijalazerovic.android_health_tracker.domain.contract.bluetooth.BluetoothScanningService
import com.danijalazerovic.android_health_tracker.domain.model.BluetoothDevice
import com.danijalazerovic.android_health_tracker.domain.model.HealthData
import com.danijalazerovic.android_health_tracker.data.history.local.HealthDataEntity
import com.danijalazerovic.android_health_tracker.data.history.local.HistoryDao
import com.danijalazerovic.android_health_tracker.domain.Resource
import com.danijalazerovic.android_health_tracker.ui.viewmodel.state.bluetooth.BluetoothCollectionError
import kotlinx.coroutines.flow.Flow

class BluetoothRepositoryImpl(
    private val scanningService: BluetoothScanningService,
    private val connectionService: BluetoothConnectionService,
    private val historyDao: HistoryDao
) : BluetoothRepository {

    override fun connectAndReadData(deviceAddress: String): Flow<HealthData> {
        return connectionService.connectAndReadData(deviceAddress)
    }

    override suspend fun saveData(healthData: HealthData): Resource<Unit> {
        return try {
            historyDao.insert(
                HealthDataEntity(
                    heartRate = healthData.heartRate ?: 0,
                    oxygenSaturation = healthData.oxygenSaturation ?: 0
                )
            )

            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e(BluetoothRepositoryImpl::class.simpleName, "Save data error", e)
            Resource.Error(BluetoothCollectionError.SavingError)
        }
    }

    override fun startScanning(): Flow<BluetoothDevice> {
        return scanningService.startScanning()
    }

    override fun stopScanning() {
        return scanningService.stopScanning()
    }
}