package com.danijalazerovic.android_health_tracker.ui.viewmodel.state.bluetooth

import com.danijalazerovic.android_health_tracker.domain.model.HealthData

data class BluetoothCollectionState(
    val isCollecting: Boolean = false,
    val isSaving: Boolean = false,
    val error: BluetoothCollectionError? = null,
    val collectedData: HealthData? = null,
)

sealed class BluetoothCollectionError : Exception() {
    data object CollectingError : BluetoothCollectionError()
    data object SavingError : BluetoothCollectionError()
}