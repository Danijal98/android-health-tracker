package com.danijalazerovic.android_health_tracker.ui.composables.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes {
    sealed class BottomNavigationItems(
        val route: String,
        val title: String,
        val icon: ImageVector,
    ) {
        data object HomeScreen : BottomNavigationItems(
            route = "home",
            title = "Home",
            icon = Icons.Outlined.Home
        )

        data object HistoryScreen : BottomNavigationItems(
            route = "history",
            title = "History",
            icon = Icons.AutoMirrored.Filled.List
        )
    }

    @Serializable
    data object Settings : Routes

    @Serializable
    data object BluetoothScanning : Routes

    @Serializable
    data class BluetoothDataCollect(val deviceAddress: String) : Routes
}