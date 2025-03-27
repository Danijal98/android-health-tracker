package com.danijalazerovic.android_health_tracker.data.history

import android.util.Log
import com.danijalazerovic.android_health_tracker.data.database.toLocalString
import com.danijalazerovic.android_health_tracker.data.history.local.HistoryDao
import com.danijalazerovic.android_health_tracker.domain.Resource
import com.danijalazerovic.android_health_tracker.domain.contract.history.HistoryRepository
import com.danijalazerovic.android_health_tracker.domain.model.HealthData
import com.danijalazerovic.android_health_tracker.ui.viewmodel.state.history.HistoryError

class HistoryRepositoryImpl(
    private val historyDao: HistoryDao,
) : HistoryRepository {
    override suspend fun getHistory(): Resource<List<HealthData>> {
        return try {
            val result = historyDao.getAll().map {
                HealthData(
                    id = it.id,
                    heartRate = it.heartRate,
                    oxygenSaturation = it.oxygenSaturation,
                    createdTime = it.createdTime.toLocalString()
                )
            }

            Resource.Success(result)
        } catch (e: Exception) {
            Log.e(HistoryRepositoryImpl::class.simpleName, "Get history error", e)
            Resource.Error(HistoryError.LoadingError)
        }
    }

    override suspend fun clearHistory(): Resource<Unit> {
        return try {
            historyDao.deleteAll()

            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e(HistoryRepositoryImpl::class.simpleName, "Clear history error", e)
            Resource.Error(HistoryError.ClearHistoryError)
        }
    }
}