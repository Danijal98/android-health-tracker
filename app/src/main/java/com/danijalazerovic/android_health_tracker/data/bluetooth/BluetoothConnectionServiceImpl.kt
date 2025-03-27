package com.danijalazerovic.android_health_tracker.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import com.danijalazerovic.android_health_tracker.domain.contract.bluetooth.BluetoothConnectionService
import com.danijalazerovic.android_health_tracker.domain.model.BluetoothConstants
import com.danijalazerovic.android_health_tracker.domain.model.HealthData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@SuppressLint("MissingPermission")
class BluetoothConnectionServiceImpl(private val context: Context) :
    BluetoothConnectionService {

    private val bluetoothManager: BluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

    private val characteristicQueue: ArrayDeque<BluetoothGattCharacteristic> = ArrayDeque()

    override fun connectAndReadData(deviceAddress: String): Flow<HealthData> = callbackFlow {
        val device =
            bluetoothAdapter?.getRemoteLeDevice(deviceAddress, BluetoothDevice.ADDRESS_TYPE_PUBLIC)
        if (device == null) {
            Log.w(
                BluetoothConnectionServiceImpl::class.simpleName,
                "Device not found for address: $deviceAddress"
            )

            close(IllegalArgumentException("Device not found for address: $deviceAddress"))
            return@callbackFlow
        }

        val gattCallback = object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    gatt?.discoverServices()
                } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    Log.w(
                        BluetoothConnectionServiceImpl::class.simpleName,
                        "Disconnected from device."
                    )

                    close(IllegalStateException("Disconnected from device."))
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                val service =
                    gatt?.getService(BluetoothConstants.HEALTH_MONITORING_SERVICE_UUID_STRING)
                val heartRateCharacteristic =
                    service?.getCharacteristic(BluetoothConstants.HEART_RATE_CHARACTERISTIC_UUID_STRING)
                val oxygenSaturationCharacteristic =
                    service?.getCharacteristic(BluetoothConstants.OXYGEN_SATURATION_CHARACTERISTIC_UUID_STRING)

                // Add all characteristics to the queue
                heartRateCharacteristic?.let { characteristicQueue.add(it) }
                oxygenSaturationCharacteristic?.let { characteristicQueue.add(it) }

                readNextCharacteristic(gatt)
            }

            override fun onCharacteristicRead(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                value: ByteArray,
                status: Int,
            ) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    val uuid = characteristic.uuid
                    when (uuid) {
                        BluetoothConstants.HEART_RATE_CHARACTERISTIC_UUID_STRING -> {
                            val heartRate = value.firstOrNull()?.toInt() ?: 0
                            trySend(HealthData(heartRate = heartRate))
                        }

                        BluetoothConstants.OXYGEN_SATURATION_CHARACTERISTIC_UUID_STRING -> {
                            val oxygenSaturation = value.firstOrNull()?.toInt() ?: 0
                            trySend(HealthData(oxygenSaturation = oxygenSaturation))
                        }
                    }
                    readNextCharacteristic(gatt)
                }
            }
        }

        val gatt = device.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE)

        awaitClose {
            gatt?.close()
        }
    }

    private fun readNextCharacteristic(gatt: BluetoothGatt?) {
        val nextCharacteristic = characteristicQueue.removeFirstOrNull()
        if (nextCharacteristic != null) {
            gatt?.readCharacteristic(nextCharacteristic)
        }
    }
}