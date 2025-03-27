package com.danijalazerovic.android_health_tracker.ui.viewmodel.event.home

sealed class HomeEvent {
    data object LoadData : HomeEvent()
}