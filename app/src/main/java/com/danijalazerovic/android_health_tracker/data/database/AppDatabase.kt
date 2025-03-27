package com.danijalazerovic.android_health_tracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.danijalazerovic.android_health_tracker.data.history.local.HistoryDao
import com.danijalazerovic.android_health_tracker.data.history.local.HealthDataEntity

@Database(entities = [HealthDataEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getHistoryDao(): HistoryDao
}