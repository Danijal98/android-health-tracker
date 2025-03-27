package com.danijalazerovic.android_health_tracker.domain.model

data class HealthData(
    val id: Long? = null,
    val heartRate: Int? = null, // (bpm)
    val oxygenSaturation: Int? = null,
    val createdTime: String? = null,
)