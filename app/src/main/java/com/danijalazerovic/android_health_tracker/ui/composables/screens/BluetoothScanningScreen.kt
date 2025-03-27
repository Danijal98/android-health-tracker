package com.danijalazerovic.android_health_tracker.ui.composables.screens

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.danijalazerovic.android_health_tracker.R
import com.danijalazerovic.android_health_tracker.domain.model.BluetoothDevice
import com.danijalazerovic.android_health_tracker.ui.composables.components.EnableBluetoothComponent
import com.danijalazerovic.android_health_tracker.ui.composables.components.RequestPermissionComponent
import com.danijalazerovic.android_health_tracker.ui.composables.components.ScannedDeviceCard
import com.danijalazerovic.android_health_tracker.ui.composables.navigation.Routes
import com.danijalazerovic.android_health_tracker.ui.viewmodel.event.bluetooth.BluetoothScanningEvent
import com.danijalazerovic.android_health_tracker.ui.viewmodel.state.bluetooth.BluetoothScanningError
import com.danijalazerovic.android_health_tracker.ui.viewmodel.state.bluetooth.BluetoothScanningState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun BluetoothScanningScreen(
    navController: NavController,
    uiState: BluetoothScanningState,
    onUiEvent: (BluetoothScanningEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.bluetooth_scanning))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        val permissionsState = rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        )
        val context = LocalContext.current
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter
        var isBluetoothEnabled by remember { mutableStateOf(bluetoothAdapter.isEnabled) }

        DisposableEffect(Unit) {
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                        val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                        isBluetoothEnabled = state == BluetoothAdapter.STATE_ON
                    }
                }
            }
            val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
            context.registerReceiver(receiver, filter)

            onDispose {
                context.unregisterReceiver(receiver)
            }
        }

        MainContentWithPermissionCheck(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            context = context,
            uiState = uiState,
            navController = navController,
            permissionsState = permissionsState,
            isBluetoothEnabled = isBluetoothEnabled,
            onUiEvent = onUiEvent
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun MainContentWithPermissionCheck(
    modifier: Modifier = Modifier,
    context: Context,
    uiState: BluetoothScanningState,
    navController: NavController,
    permissionsState: MultiplePermissionsState,
    isBluetoothEnabled: Boolean,
    onUiEvent: (BluetoothScanningEvent) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (permissionsState.allPermissionsGranted) {
            MainContent(
                context = context,
                uiState = uiState,
                navController = navController,
                isBluetoothEnabled = isBluetoothEnabled,
                onUiEvent = onUiEvent
            )
        } else {
            RequestPermissionComponent(
                modifier = Modifier.fillMaxSize(),
                permissionsState = permissionsState,
                title = stringResource(R.string.bluetooth_permission_missing),
                buttonText = stringResource(R.string.bluetooth_grant_permission)
            )
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
private fun ColumnScope.MainContent(
    context: Context,
    uiState: BluetoothScanningState,
    navController: NavController,
    isBluetoothEnabled: Boolean,
    onUiEvent: (BluetoothScanningEvent) -> Unit,
) {
    if (isBluetoothEnabled) {
        val uiError = uiState.error
        if (uiError != null) {
            when (uiError) {
                BluetoothScanningError.ScanningError -> Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.scanning_error)
                )

                BluetoothScanningError.DefaultError -> Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.unknown_error)
                )
            }
        } else {
            // Show Bluetooth Scanning UI if permissions are granted and there is no error
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.scannedDevices) { device ->
                    ScannedDeviceCard(
                        modifier = Modifier.fillMaxWidth(),
                        device = device,
                        onClick = {
                            navController.navigate(Routes.BluetoothDataCollect(it.address))
                        }
                    )
                }
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (uiState.isScanning) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
            ),
            onClick = {
                if (uiState.isScanning) onUiEvent(BluetoothScanningEvent.StopScanning)
                else onUiEvent(BluetoothScanningEvent.StartScanning)
            }
        ) {
            if (uiState.isScanning) Text(stringResource(R.string.stop_scanning))
            else Text(stringResource(R.string.start_scanning))
        }
    } else {
        // Prompt user to enable Bluetooth
        EnableBluetoothComponent(
            modifier = Modifier.fillMaxSize(),
            context = context
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenDefaultPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainContent(
            context = LocalContext.current,
            uiState = BluetoothScanningState(),
            navController = rememberNavController(),
            isBluetoothEnabled = true,
            onUiEvent = {}
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview(showBackground = true)
@Composable
private fun ScreenBluetoothPermissionPreview() {
    MainContentWithPermissionCheck(
        context = LocalContext.current,
        uiState = BluetoothScanningState(),
        navController = rememberNavController(),
        permissionsState = rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        ),
        isBluetoothEnabled = false,
        onUiEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ScreenDefaultBluetoothDisabledPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainContent(
            context = LocalContext.current,
            uiState = BluetoothScanningState(),
            navController = rememberNavController(),
            isBluetoothEnabled = false,
            onUiEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenScanningPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainContent(
            context = LocalContext.current,
            uiState = BluetoothScanningState(
                isScanning = true,
                scannedDevices = listOf()
            ),
            navController = rememberNavController(),
            isBluetoothEnabled = true,
            onUiEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenScanningWithDataPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainContent(
            context = LocalContext.current,
            uiState = BluetoothScanningState(
                isScanning = true,
                scannedDevices = listOf(
                    BluetoothDevice(
                        name = "Device 1",
                        address = "Address 1",
                        signalStrength = 20
                    ),
                    BluetoothDevice(
                        name = "Device 2",
                        address = "Address 2",
                        signalStrength = 30
                    )
                )
            ),
            navController = rememberNavController(),
            isBluetoothEnabled = true,
            onUiEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenScanningErrorPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainContent(
            context = LocalContext.current,
            uiState = BluetoothScanningState(
                isScanning = false,
                error = BluetoothScanningError.ScanningError,
                scannedDevices = listOf()
            ),
            navController = rememberNavController(),
            isBluetoothEnabled = true,
            onUiEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenUnknownErrorPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainContent(
            context = LocalContext.current,
            uiState = BluetoothScanningState(
                isScanning = false,
                error = BluetoothScanningError.DefaultError,
                scannedDevices = listOf()
            ),
            navController = rememberNavController(),
            isBluetoothEnabled = true,
            onUiEvent = {}
        )
    }
}