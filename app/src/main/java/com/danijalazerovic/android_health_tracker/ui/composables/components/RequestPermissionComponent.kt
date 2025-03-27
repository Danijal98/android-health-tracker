package com.danijalazerovic.android_health_tracker.ui.composables.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissionComponent(
    modifier: Modifier = Modifier,
    permissionsState: MultiplePermissionsState,
    title: String,
    buttonText: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(title)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { permissionsState.launchMultiplePermissionRequest() }) {
            Text(buttonText)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview(showBackground = true)
@Composable
private fun RequestPermissionComponentPreview() {
    RequestPermissionComponent(
        modifier = Modifier.fillMaxSize(),
        permissionsState = rememberMultiplePermissionsState(
            listOf()
        ),
        title = "Bluetooth permission is missing",
        buttonText = "Grant Bluetooth permission"
    )
}