package com.danijalazerovic.android_health_tracker.ui.viewmodel.event.bluetooth

sealed class BluetoothCollectionUIEvent {
    data object SaveSuccessful : BluetoothCollectionUIEvent()
    data object SaveUnSuccessful : BluetoothCollectionUIEvent()
}