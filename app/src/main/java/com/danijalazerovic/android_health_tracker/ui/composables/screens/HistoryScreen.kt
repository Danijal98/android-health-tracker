package com.danijalazerovic.android_health_tracker.ui.composables.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danijalazerovic.android_health_tracker.R
import com.danijalazerovic.android_health_tracker.domain.model.HealthData
import com.danijalazerovic.android_health_tracker.ui.viewmodel.event.history.HistoryEvent
import com.danijalazerovic.android_health_tracker.ui.viewmodel.event.history.HistoryUIEvent
import com.danijalazerovic.android_health_tracker.ui.composables.components.DataLoadFailedState
import com.danijalazerovic.android_health_tracker.ui.composables.components.HistoryListItemCard
import com.danijalazerovic.android_health_tracker.ui.composables.components.NoDataState
import com.danijalazerovic.android_health_tracker.ui.composables.components.UnknownErrorState
import com.danijalazerovic.android_health_tracker.ui.viewmodel.state.history.HistoryError
import com.danijalazerovic.android_health_tracker.ui.viewmodel.state.history.HistoryState
import kotlinx.coroutines.flow.SharedFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    uiState: HistoryState,
    uiEvent: SharedFlow<HistoryUIEvent>,
    onUiEvent: (HistoryEvent) -> Unit,
) {
    val snackBarHost = remember {
        SnackbarHostState()
    }
    var displayMenu by remember { mutableStateOf(false) }
    val historyClearedString = stringResource(R.string.history_cleared)

    LaunchedEffect(Unit) {
        onUiEvent(HistoryEvent.LoadData)
        uiEvent.collect { event ->
            when (event) {
                HistoryUIEvent.HistoryClearedSuccessfully -> snackBarHost.showSnackbar(
                    historyClearedString
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.history))
                },
                actions = {
                    IconButton(onClick = { displayMenu = !displayMenu }) {
                        Icon(Icons.Default.MoreVert, "")
                    }

                    DropdownMenu(
                        expanded = displayMenu,
                        onDismissRequest = { displayMenu = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                onUiEvent(HistoryEvent.ClearHistory)
                                displayMenu = false
                            },
                            text = { Text(stringResource(R.string.clear_history)) }
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHost)
        }
    ) { paddingValues ->
        MainContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            uiState = uiState,
            onUiEvent = onUiEvent
        )
    }
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    uiState: HistoryState,
    onUiEvent: (HistoryEvent) -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val error = uiState.error
        if (error != null) {
            when (error) {
                HistoryError.LoadingError -> DataLoadFailedState {
                    onUiEvent(HistoryEvent.LoadData)
                }

                HistoryError.DefaultError -> UnknownErrorState {
                    onUiEvent(HistoryEvent.LoadData)
                }

                else -> {}
            }
        } else if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.historyList.isEmpty()) {
            NoDataState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.historyList, key = { it.id ?: 0 }) { item ->
                    HistoryListItemCard(healthData = item)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenDefaultPreview() {
    MainContent(
        uiState = HistoryState(),
        onUiEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ScreenLoadingPreview() {
    MainContent(
        uiState = HistoryState(
            isLoading = true
        ),
        onUiEvent = {}
    )
}

@Preview
@Composable
private fun ScreenDataPreview() {
    MainContent(
        uiState = HistoryState(
            historyList = listOf(
                HealthData(
                    0,
                    80,
                    100
                ),
                HealthData(
                    1,
                    90,
                    90
                )
            )
        ),
        onUiEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ScreenLoadingErrorPreview() {
    MainContent(
        uiState = HistoryState(
            isLoading = false,
            error = HistoryError.LoadingError
        ),
        onUiEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ScreenDefaultErrorPreview() {
    MainContent(
        uiState = HistoryState(
            isLoading = false,
            error = HistoryError.DefaultError
        ),
        onUiEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ScreenErrorWithDataPreview() {
    MainContent(
        uiState = HistoryState(
            isLoading = false,
            historyList = listOf(
                HealthData(
                    80,
                    100
                ),
                HealthData(
                    90,
                    90
                )
            ),
            error = HistoryError.LoadingError
        ),
        onUiEvent = {}
    )
}