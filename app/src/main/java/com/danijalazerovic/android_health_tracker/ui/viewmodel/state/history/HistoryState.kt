package com.danijalazerovic.android_health_tracker.ui.viewmodel.state.history

import com.danijalazerovic.android_health_tracker.domain.model.HealthData

data class HistoryState(
    val isLoading: Boolean = false,
    val error: HistoryError? = null,
    val historyList: List<HealthData> = emptyList(),
)

sealed class HistoryError : Exception() {
    data object LoadingError : HistoryError()
    data object ClearHistoryError : HistoryError()
    data object DefaultError : HistoryError()
}