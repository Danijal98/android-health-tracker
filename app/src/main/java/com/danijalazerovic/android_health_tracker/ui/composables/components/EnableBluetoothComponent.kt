package com.danijalazerovic.android_health_tracker.ui.composables.components

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.danijalazerovic.android_health_tracker.R

@SuppressLint("MissingPermission")
@Composable
fun EnableBluetoothComponent(modifier: Modifier = Modifier, context: Context) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(stringResource(R.string.bluetooth_turned_off))
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            context.startActivity(enableBtIntent)
        }) {
            Text(stringResource(R.string.bluetooth_turn_on))
        }
    }
}