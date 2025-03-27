package com.danijalazerovic.android_health_tracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danijalazerovic.android_health_tracker.domain.contract.bluetooth.BluetoothRepository
import com.danijalazerovic.android_health_tracker.domain.model.BluetoothDevice
import com.danijalazerovic.android_health_tracker.ui.viewmodel.event.bluetooth.BluetoothScanningEvent
import com.danijalazerovic.android_health_tracker.ui.viewmodel.state.bluetooth.BluetoothScanningError
import com.danijalazerovic.android_health_tracker.ui.viewmodel.state.bluetooth.BluetoothScanningState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BluetoothScanningViewModel(
    private val bluetoothRepository: BluetoothRepository,
) : ViewModel() {
    private val scannedDevices: MutableMap<String, BluetoothDevice> = mutableMapOf()

    private val _state = MutableStateFlow(BluetoothScanningState())
    val state: StateFlow<BluetoothScanningState> = _state

    private var scanningJob: Job? = null

    fun onEvent(event: BluetoothScanningEvent) = when (event) {
        BluetoothScanningEvent.StartScanning -> startScanning()
        BluetoothScanningEvent.StopScanning -> stopScanning()
    }

    private fun stopScanning() {
        try {
            bluetoothRepository.stopScanning()
            scanningJob?.cancel()
            _state.update {
                it.copy(
                    isScanning = false,
                    scannedDevices = emptyList()
                )
            }
            scannedDevices.clear()
        } catch (e: Exception) {
            println("Stop scanning exception: ${e.message}")
        }
    }

    private fun startScanning() {
        scanningJob = viewModelScope.launch {
            bluetoothRepository.startScanning()
                .onStart {
                    _state.update {
                        it.copy(
                            isScanning = true
                        )
                    }
                }
                .catch {
                    _state.update {
                        it.copy(
                            isScanning = false,
                            error = BluetoothScanningError.ScanningError
                        )
                    }
                }
                .collect { device ->
                    scannedDevices[device.address] =
                        device.copy(signalStrength = device.signalStrength)
                    _state.update {
                        it.copy(
                            scannedDevices = scannedDevices.values.toList()
                        )
                    }
                }
        }
    }
}