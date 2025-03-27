package com.danijalazerovic.android_health_tracker.domain.contract.history

import com.danijalazerovic.android_health_tracker.domain.Resource
import com.danijalazerovic.android_health_tracker.domain.model.HealthData

interface HistoryRepository {
    suspend fun getHistory(): Resource<List<HealthData>>
    suspend fun clearHistory(): Resource<Unit>
}