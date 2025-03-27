package com.danijalazerovic.android_health_tracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danijalazerovic.android_health_tracker.domain.Resource
import com.danijalazerovic.android_health_tracker.domain.contract.home.UserDetailsRepository
import com.danijalazerovic.android_health_tracker.ui.viewmodel.event.home.HomeEvent
import com.danijalazerovic.android_health_tracker.ui.viewmodel.state.home.HomeError
import com.danijalazerovic.android_health_tracker.ui.viewmodel.state.home.HomeState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userDetailsRepository: UserDetailsRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    private var loadDataJob: Job? = null

    fun onEvent(event: HomeEvent) = when (event) {
        HomeEvent.LoadData -> loadData()
    }

    private fun loadData() {
        _state.update {
            it.copy(
                homeError = null,
                isLoading = true
            )
        }

        loadDataJob = viewModelScope.launch {
            when (val result = userDetailsRepository.getUserDetails()) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userDetails = result.data
                        )
                    }
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            homeError = result.exception as? HomeError ?: HomeError.DefaultError
                        )
                    }
                }
            }

        }
    }
}