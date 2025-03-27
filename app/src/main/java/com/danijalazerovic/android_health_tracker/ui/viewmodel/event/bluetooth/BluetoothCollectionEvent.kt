package com.danijalazerovic.android_health_tracker.ui.viewmodel.event.bluetooth

import com.danijalazerovic.android_health_tracker.domain.model.HealthData

sealed class BluetoothCollectionEvent {
    data class CollectData(val deviceAddress: String) : BluetoothCollectionEvent()
    data class SaveData(val healthData: HealthData) : BluetoothCollectionEvent()
}