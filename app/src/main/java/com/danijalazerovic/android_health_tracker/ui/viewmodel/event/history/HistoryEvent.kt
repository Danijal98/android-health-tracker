package com.danijalazerovic.android_health_tracker.ui.viewmodel.event.history

sealed class HistoryEvent {
    data object LoadData : HistoryEvent()
    data object ClearHistory : HistoryEvent()
}