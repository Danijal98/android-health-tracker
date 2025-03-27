package com.danijalazerovic.android_health_tracker.ui.composables.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoaderButton(
    modifier: Modifier = Modifier,
    text: String,
    isEnabled: Boolean = true,
    isLoading: Boolean,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .height(40.dp)
            .padding(start = 8.dp),
        onClick = { onClick() },
        enabled = !isLoading && isEnabled
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text(text)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoaderButtonPreview() {
    LoaderButton(text = "Save", isLoading = false) {}
}

@Preview(showBackground = true)
@Composable
private fun LoaderButtonLoadingPreview() {
    LoaderButton(text = "Save", isLoading = true) {}
}