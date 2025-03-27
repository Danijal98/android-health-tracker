package com.danijalazerovic.android_health_tracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danijalazerovic.android_health_tracker.domain.Resource
import com.danijalazerovic.android_health_tracker.domain.model.HealthData
import com.danijalazerovic.android_health_tracker.ui.viewmodel.state.bluetooth.BluetoothCollectionError
import com.danijalazerovic.android_health_tracker.ui.viewmodel.state.bluetooth.BluetoothCollectionState
import com.danijalazerovic.android_health_tracker.domain.contract.bluetooth.BluetoothRepository
import com.danijalazerovic.android_health_tracker.ui.viewmodel.event.bluetooth.BluetoothCollectionEvent
import com.danijalazerovic.android_health_tracker.ui.viewmodel.event.bluetooth.BluetoothCollectionUIEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BluetoothCollectionViewModel(
    private val bluetoothRepository: BluetoothRepository,
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<BluetoothCollectionUIEvent>()
    val uiEvent: SharedFlow<BluetoothCollectionUIEvent> = _uiEvent

    private val _state = MutableStateFlow(BluetoothCollectionState())
    val state: StateFlow<BluetoothCollectionState> = _state

    private var collectDataJob: Job? = null
    private var saveDataJob: Job? = null

    fun onEvent(event: BluetoothCollectionEvent) = when (event) {
        is BluetoothCollectionEvent.CollectData -> collectData(event.deviceAddress)
        is BluetoothCollectionEvent.SaveData -> saveData(event.healthData)
    }

    private fun saveData(healthData: HealthData) {
        _state.update {
            it.copy(
                isSaving = true
            )
        }

        saveDataJob = viewModelScope.launch {
            when (bluetoothRepository.saveData(healthData)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isSaving = false
                        )
                    }
                    _uiEvent.emit(BluetoothCollectionUIEvent.SaveSuccessful)
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isSaving = false
                        )
                    }
                    _uiEvent.emit(BluetoothCollectionUIEvent.SaveUnSuccessful)
                }
            }
        }
    }

    private fun collectData(deviceAddress: String) {
        collectDataJob = viewModelScope.launch {
            bluetoothRepository.connectAndReadData(deviceAddress)
                .onStart {
                    _state.update {
                        it.copy(
                            error = null,
                            isCollecting = true
                        )
                    }
                }
                .catch {
                    _state.update {
                        it.copy(
                            isCollecting = false,
                            error = BluetoothCollectionError.CollectingError
                        )
                    }
                }
                .collect { data ->
                    val collectedData = _state.value.collectedData

                    // Update only non-null values
                    _state.update {
                        it.copy(
                            collectedData = collectedData?.copy(
                                heartRate = data.heartRate ?: collectedData.heartRate,
                                oxygenSaturation = data.oxygenSaturation
                                    ?: collectedData.oxygenSaturation
                            ) ?: HealthData(
                                heartRate = data.heartRate,
                                oxygenSaturation = data.oxygenSaturation
                            )
                        )
                    }

                    val dataAfterUpdate = _state.value.collectedData
                    // Stop collecting if both heartRate and oxygenSaturation are already set
                    if (dataAfterUpdate?.heartRate != null && dataAfterUpdate.oxygenSaturation != null) {
                        _state.update {
                            it.copy(isCollecting = false)
                        }

                        this.cancel()
                    }
                }
        }
    }
}