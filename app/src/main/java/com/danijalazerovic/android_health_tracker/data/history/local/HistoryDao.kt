package com.danijalazerovic.android_health_tracker.data.history.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface HistoryDao {
    // Insert a single health data entry
    @Insert
    suspend fun insert(healthData: HealthDataEntity): Long

    // Insert multiple health data entries
    @Insert
    suspend fun insertAll(healthDataList: List<HealthDataEntity>): List<Long>

    // Update an existing health data entry
    @Update
    suspend fun update(healthData: HealthDataEntity)

    // Delete a specific health data entry
    @Delete
    suspend fun delete(healthData: HealthDataEntity)

    // Retrieve all health data entries
    @Query("SELECT * FROM HealthDataEntity")
    suspend fun getAll(): List<HealthDataEntity>

    // Retrieve a health data entry by ID
    @Query("SELECT * FROM HealthDataEntity WHERE id = :id")
    suspend fun getById(id: Long): HealthDataEntity?

    // Delete all health data entries
    @Query("DELETE FROM HealthDataEntity")
    suspend fun deleteAll()
}