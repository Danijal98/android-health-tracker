package com.danijalazerovic.android_health_tracker.ui.composables.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danijalazerovic.android_health_tracker.R

@Composable
private fun ImageWithText(
    modifier: Modifier = Modifier,
    image: Painter,
    text: String,
    actions: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = image,
            contentDescription = null
        )

        Text(text)

        actions()
    }
}

@Composable
fun NoDataState(modifier: Modifier = Modifier) {
    ImageWithText(
        modifier = modifier,
        image = painterResource(R.drawable.no_data),
        text = stringResource(R.string.no_data)
    )
}

@Composable
fun DataLoadFailedState(modifier: Modifier = Modifier, retry: () -> Unit) {
    ImageWithText(
        modifier = modifier,
        image = painterResource(R.drawable.load_failed),
        text = stringResource(R.string.load_failed),
        actions = {
            Button(onClick = retry) {
                Text(stringResource(R.string.retry))
            }
        }
    )
}

@Composable
fun UnknownErrorState(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.unknown_error),
    retry: () -> Unit,
) {
    ImageWithText(
        modifier = modifier,
        image = painterResource(R.drawable.unknown_error),
        text = text,
        actions = {
            Button(onClick = retry) {
                Text(stringResource(R.string.retry))
            }
        }
    )
}

@Preview(showSystemUi = true, showBackground = false, backgroundColor = 0xFF9C27B0)
@Composable
private fun NoDataStatePreview() {
    NoDataState()
}

@Preview(showSystemUi = true)
@Composable
private fun DataLoadFailedStatePreview() {
    DataLoadFailedState {  }
}

@Preview(showSystemUi = true)
@Composable
private fun UnknownErrorStatePreview() {
    UnknownErrorState {  }
}