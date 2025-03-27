package com.danijalazerovic.android_health_tracker.ui.composables.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.danijalazerovic.android_health_tracker.ui.composables.screens.BluetoothDataCollectScreen
import com.danijalazerovic.android_health_tracker.ui.composables.screens.BluetoothScanningScreen
import com.danijalazerovic.android_health_tracker.ui.composables.screens.HistoryScreen
import com.danijalazerovic.android_health_tracker.ui.composables.screens.HomeScreen
import com.danijalazerovic.android_health_tracker.ui.composables.screens.SettingsScreen
import com.danijalazerovic.android_health_tracker.ui.viewmodel.BluetoothCollectionViewModel
import com.danijalazerovic.android_health_tracker.ui.viewmodel.BluetoothScanningViewModel
import com.danijalazerovic.android_health_tracker.ui.viewmodel.HistoryViewModel
import com.danijalazerovic.android_health_tracker.ui.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Routes.BottomNavigationItems.HomeScreen.route) {
        composable(Routes.BottomNavigationItems.HomeScreen.route) {
            val vm: HomeViewModel = koinViewModel()
            val uiState = vm.state.collectAsStateWithLifecycle().value

            HomeScreen(
                navController = navController,
                uiState = uiState,
                onUiEvent = vm::onEvent
            )
        }
        composable(Routes.BottomNavigationItems.HistoryScreen.route) {
            val vm: HistoryViewModel = koinViewModel()
            val uiState = vm.state.collectAsStateWithLifecycle().value
            val uiEvent = vm.uiEvent

            HistoryScreen(
                uiState = uiState,
                uiEvent = uiEvent,
                onUiEvent = vm::onEvent
            )
        }
        composable<Routes.Settings> {
            SettingsScreen(navController)
        }
        composable<Routes.BluetoothScanning> {
            val vm: BluetoothScanningViewModel = koinViewModel()
            val uiState = vm.state.collectAsStateWithLifecycle().value

            BluetoothScanningScreen(
                navController = navController,
                uiState = uiState,
                onUiEvent = vm::onEvent
            )
        }
        composable<Routes.BluetoothDataCollect> { backStackEntry ->
            val bluetoothDataCollect: Routes.BluetoothDataCollect = backStackEntry.toRoute()
            val vm: BluetoothCollectionViewModel = koinViewModel()
            val uiState = vm.state.collectAsStateWithLifecycle().value
            val uiEvent = vm.uiEvent

            BluetoothDataCollectScreen(
                navController = navController,
                deviceAddress = bluetoothDataCollect.deviceAddress,
                uiState = uiState,
                uiEvent = uiEvent,
                onUiEvent = vm::onEvent
            )
        }
    }
}