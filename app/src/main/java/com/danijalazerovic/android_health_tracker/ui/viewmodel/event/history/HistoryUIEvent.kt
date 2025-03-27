package com.danijalazerovic.android_health_tracker.ui.viewmodel.event.history

sealed class HistoryUIEvent {
    data object HistoryClearedSuccessfully : HistoryUIEvent()
}