package com.danijalazerovic.android_health_tracker.ui.composables.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danijalazerovic.android_health_tracker.R
import com.danijalazerovic.android_health_tracker.domain.model.BluetoothDevice

@Composable
fun ScannedDeviceCard(
    modifier: Modifier = Modifier,
    device: BluetoothDevice,
    onClick: (device: BluetoothDevice) -> Unit,
) {
    Card(
        modifier = modifier
            .clickable { onClick(device) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.scanned_device_name, device.name),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.scanned_device_address, device.address),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            RssiSignalIndicator(
                modifier = Modifier.size(40.dp),
                rssi = device.signalStrength
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScannedDeviceCardPreview() {
    ScannedDeviceCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        device = BluetoothDevice(
            name = "Device name",
            address = "Device address",
            signalStrength = -20
        )
    ) { }
}