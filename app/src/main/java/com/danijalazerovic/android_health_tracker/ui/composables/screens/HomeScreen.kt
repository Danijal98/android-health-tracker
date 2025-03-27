package com.danijalazerovic.android_health_tracker.ui.composables.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.danijalazerovic.android_health_tracker.R
import com.danijalazerovic.android_health_tracker.domain.model.UserDetails
import com.danijalazerovic.android_health_tracker.ui.viewmodel.event.home.HomeEvent
import com.danijalazerovic.android_health_tracker.ui.viewmodel.state.home.HomeError
import com.danijalazerovic.android_health_tracker.ui.viewmodel.state.home.HomeState
import com.danijalazerovic.android_health_tracker.ui.composables.components.AverageHealthMetricCard
import com.danijalazerovic.android_health_tracker.ui.composables.components.DataLoadFailedState
import com.danijalazerovic.android_health_tracker.ui.composables.components.NoDataState
import com.danijalazerovic.android_health_tracker.ui.composables.components.UnknownErrorState
import com.danijalazerovic.android_health_tracker.ui.composables.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    uiState: HomeState,
    onUiEvent: (HomeEvent) -> Unit,
) {
    LaunchedEffect(Unit) {
        onUiEvent(HomeEvent.LoadData)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.home))
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Routes.Settings)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings"
                        )
                    }
                })
        },
        floatingActionButton = {
            Button(onClick = {
                navController.navigate(Routes.BluetoothScanning)
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Bluetooth")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            val data = uiState.userDetails
            val error = uiState.homeError

            if (error != null) {
                when(error) {
                    HomeError.LoadingError -> DataLoadFailedState {
                        onUiEvent(HomeEvent.LoadData)
                    }
                    HomeError.DefaultError -> UnknownErrorState {
                        onUiEvent(HomeEvent.LoadData)
                    }
                }
            }
            else if (data != null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AverageHealthMetricCard(
                        title = stringResource(R.string.heart_rate),
                        averageValue = data.averageHeartRate,
                        minValue = 40,
                        maxValue = 185,
                        unit = stringResource(R.string.bpm),
                        icon = painterResource(R.drawable.heart_rate)
                    )

                    AverageHealthMetricCard(
                        title = stringResource(R.string.oxygen_saturation),
                        averageValue = data.averageOxygenSaturation,
                        minValue = 80,
                        maxValue = 100,
                        unit = stringResource(R.string.percentage_sign),
                        icon = painterResource(R.drawable.oxygen)
                    )
                }
            }
            else if (uiState.isLoading) {
                CircularProgressIndicator()
            }  else {
                NoDataState()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenDefaultPreview() {
    MaterialTheme {
        HomeScreen(
            navController = rememberNavController(),
            uiState = HomeState(),
            onUiEvent = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenLoadingPreview() {
    MaterialTheme {
        HomeScreen(
            navController = rememberNavController(),
            uiState = HomeState(
                isLoading = true,
                userDetails = null
            ),
            onUiEvent = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenWithDataPreview() {
    MaterialTheme {
        HomeScreen(
            navController = rememberNavController(),
            uiState = HomeState(
                isLoading = false,
                userDetails = UserDetails(
                    averageOxygenSaturation = 90,
                    averageHeartRate = 80
                ),
            ),
            onUiEvent = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenLoadingErrorPreview() {
    MaterialTheme {
        HomeScreen(
            navController = rememberNavController(),
            uiState = HomeState(
                isLoading = false,
                userDetails = null,
                homeError = HomeError.LoadingError
            ),
            onUiEvent = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenDefaultErrorPreview() {
    MaterialTheme {
        HomeScreen(
            navController = rememberNavController(),
            uiState = HomeState(
                isLoading = false,
                userDetails = null,
                homeError = HomeError.DefaultError
            ),
            onUiEvent = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenErrorWithDataPreview() {
    MaterialTheme {
        HomeScreen(
            navController = rememberNavController(),
            uiState = HomeState(
                isLoading = false,
                userDetails = UserDetails(
                    averageOxygenSaturation = 90,
                    averageHeartRate = 80
                ),
                homeError = HomeError.LoadingError
            ),
            onUiEvent = { }
        )
    }
}