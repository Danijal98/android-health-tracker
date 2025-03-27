package com.danijalazerovic.android_health_tracker.data.history.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity
data class HealthDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val heartRate: Int, // (bpm)
    val oxygenSaturation: Int,
    val createdTime: Instant = Clock.System.now()
)