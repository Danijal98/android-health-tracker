package com.danijalazerovic.android_health_tracker.ui.viewmodel.state.home

import com.danijalazerovic.android_health_tracker.domain.model.UserDetails

data class HomeState(
    val isLoading: Boolean = false,
    val homeError: HomeError? = null,
    val userDetails: UserDetails? = null
)

sealed class HomeError : Exception() {
    data object LoadingError : HomeError()
    data object DefaultError : HomeError()
}