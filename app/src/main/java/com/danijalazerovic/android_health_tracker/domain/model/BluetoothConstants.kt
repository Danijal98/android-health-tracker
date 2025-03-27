package com.danijalazerovic.android_health_tracker.domain.model

import java.util.UUID

object BluetoothConstants {
    val HEALTH_MONITORING_SERVICE_UUID_STRING: UUID = UUID.fromString("4ce0155f-4a20-4454-ae9b-3cdf13ea624d")
    val HEART_RATE_CHARACTERISTIC_UUID_STRING: UUID = UUID.fromString("d120e864-9b6a-4aeb-b3bc-376402dc449f")
    val OXYGEN_SATURATION_CHARACTERISTIC_UUID_STRING: UUID = UUID.fromString("944bd739-ed43-4eb6-8b05-232b9bc54224")
}