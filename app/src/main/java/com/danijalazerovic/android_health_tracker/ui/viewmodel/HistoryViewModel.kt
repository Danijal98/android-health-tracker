package com.danijalazerovic.android_health_tracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danijalazerovic.android_health_tracker.domain.Resource
import com.danijalazerovic.android_health_tracker.domain.contract.history.HistoryRepository
import com.danijalazerovic.android_health_tracker.ui.viewmodel.event.history.HistoryEvent
import com.danijalazerovic.android_health_tracker.ui.viewmodel.state.history.HistoryError
import com.danijalazerovic.android_health_tracker.ui.viewmodel.state.history.HistoryState
import com.danijalazerovic.android_health_tracker.ui.viewmodel.event.history.HistoryUIEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val historyRepository: HistoryRepository,
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<HistoryUIEvent>()
    val uiEvent: SharedFlow<HistoryUIEvent> = _uiEvent

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state

    private var historyJob: Job? = null
    private var clearHistoryJob: Job? = null

    fun onEvent(event: HistoryEvent) = when (event) {
        HistoryEvent.LoadData -> loadData()
        HistoryEvent.ClearHistory -> clearHistory()
    }

    private fun clearHistory() {
        if (_state.value.historyList.isEmpty()) return

        _state.update {
            it.copy(
                isLoading = true
            )
        }

        clearHistoryJob = viewModelScope.launch {
            when (val result = historyRepository.clearHistory()) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            historyList = emptyList()
                        )
                    }
                    _uiEvent.emit(HistoryUIEvent.HistoryClearedSuccessfully)
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception as? HistoryError ?: HistoryError.DefaultError
                        )
                    }
                }
            }
        }
    }

    private fun loadData() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }

        historyJob = viewModelScope.launch {
            when (val result = historyRepository.getHistory()) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            historyList = result.data
                        )
                    }
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception as? HistoryError ?: HistoryError.DefaultError
                        )
                    }
                }
            }
        }
    }
}