package com.danijalazerovic.android_health_tracker.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDetails(
    val averageHeartRate: Int,
    val averageOxygenSaturation: Int
)