package com.danijalazerovic.android_health_tracker.ui.composables.screens

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import com.danijalazerovic.android_health_tracker.R
import com.danijalazerovic.android_health_tracker.domain.model.HealthData
import com.danijalazerovic.android_health_tracker.ui.viewmodel.event.bluetooth.BluetoothCollectionEvent
import com.danijalazerovic.android_health_tracker.ui.viewmodel.event.bluetooth.BluetoothCollectionUIEvent
import com.danijalazerovic.android_health_tracker.ui.composables.components.DataCollectedCard
import com.danijalazerovic.android_health_tracker.ui.composables.components.EnableBluetoothComponent
import com.danijalazerovic.android_health_tracker.ui.composables.components.LoaderButton
import com.danijalazerovic.android_health_tracker.ui.composables.components.RequestPermissionComponent
import com.danijalazerovic.android_health_tracker.ui.composables.components.UnknownErrorState
import com.danijalazerovic.android_health_tracker.ui.viewmodel.state.bluetooth.BluetoothCollectionError
import com.danijalazerovic.android_health_tracker.ui.viewmodel.state.bluetooth.BluetoothCollectionState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.flow.SharedFlow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun BluetoothDataCollectScreen(
    navController: NavController,
    deviceAddress: String,
    uiState: BluetoothCollectionState,
    uiEvent: SharedFlow<BluetoothCollectionUIEvent>,
    onUiEvent: (BluetoothCollectionEvent) -> Unit,
) {
    val saveSuccessString = stringResource(R.string.save_success)
    val saveFailedString = stringResource(R.string.save_fail)
    val snackBarHost = remember {
        SnackbarHostState()
    }

    LaunchedEffect(Unit) {
        uiEvent.collect { event ->
            when (event) {
                BluetoothCollectionUIEvent.SaveSuccessful -> snackBarHost.showSnackbar(
                    saveSuccessString
                )

                BluetoothCollectionUIEvent.SaveUnSuccessful -> snackBarHost.showSnackbar(
                    saveFailedString
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.bluetooth_data_collect_title, deviceAddress))
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
        snackbarHost = {
            SnackbarHost(hostState = snackBarHost)
        }
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
            deviceAddress = deviceAddress,
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
    uiState: BluetoothCollectionState,
    deviceAddress: String,
    permissionsState: MultiplePermissionsState,
    isBluetoothEnabled: Boolean,
    onUiEvent: (BluetoothCollectionEvent) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (permissionsState.allPermissionsGranted) {
            MainContent(
                context = context,
                uiState = uiState,
                deviceAddress = deviceAddress,
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
    uiState: BluetoothCollectionState,
    deviceAddress: String,
    isBluetoothEnabled: Boolean,
    onUiEvent: (BluetoothCollectionEvent) -> Unit,
) {
    if (isBluetoothEnabled) {
        val uiError = uiState.error
        val data = uiState.collectedData

        if (uiError != null) {
            when (uiError) {
                BluetoothCollectionError.CollectingError -> UnknownErrorState(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.collecting_error)
                ) {
                    onUiEvent(BluetoothCollectionEvent.CollectData(deviceAddress))
                }

                else -> {}
            }
        } else {
            data?.let {
                DataCollectedCard(
                    modifier = Modifier.fillMaxWidth(),
                    data = it,
                    isSaving = uiState.isSaving,
                    onSave = { data -> onUiEvent(BluetoothCollectionEvent.SaveData(data)) }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            LoaderButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.collect_data),
                isLoading = uiState.isCollecting,
                onClick = {
                    onUiEvent(BluetoothCollectionEvent.CollectData(deviceAddress))
                }
            )
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainContent(
            context = LocalContext.current,
            uiState = BluetoothCollectionState(),
            deviceAddress = "some random device address",
            isBluetoothEnabled = true,
            onUiEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenCollectingPreview() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainContent(
            context = LocalContext.current,
            uiState = BluetoothCollectionState(
                isCollecting = true
            ),
            deviceAddress = "some random device address",
            isBluetoothEnabled = true,
            onUiEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenDataPreview() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainContent(
            context = LocalContext.current,
            uiState = BluetoothCollectionState(
                isCollecting = false,
                collectedData = HealthData(
                    heartRate = 80,
                    oxygenSaturation = 100
                )
            ),
            deviceAddress = "some random device address",
            isBluetoothEnabled = true,
            onUiEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenSavingDataPreview() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainContent(
            context = LocalContext.current,
            uiState = BluetoothCollectionState(
                isCollecting = false,
                collectedData = HealthData(
                    heartRate = 80,
                    oxygenSaturation = 100
                ),
                isSaving = true
            ),
            deviceAddress = "some random device address",
            isBluetoothEnabled = true,
            onUiEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenCollectingErrorPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainContent(
            context = LocalContext.current,
            uiState = BluetoothCollectionState(
                isCollecting = false,
                collectedData = HealthData(
                    heartRate = 80,
                    oxygenSaturation = 100
                ),
                error = BluetoothCollectionError.CollectingError
            ),
            deviceAddress = "some random device address",
            isBluetoothEnabled = true,
            onUiEvent = {}
        )
    }
}