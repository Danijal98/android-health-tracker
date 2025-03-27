package com.danijalazerovic.android_health_tracker.domain.model

data class BluetoothDevice(
    val name: String,
    val address: String,
    val signalStrength: Int
)