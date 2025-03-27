package com.danijalazerovic.android_health_tracker.ui.composables.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danijalazerovic.android_health_tracker.R
import com.danijalazerovic.android_health_tracker.domain.model.HealthData

@Composable
fun HistoryListItemCard(
    modifier: Modifier = Modifier,
    healthData: HealthData,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.data),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = stringResource(R.string.recorded_data_time, healthData.createdTime ?: "/"),
                    style = MaterialTheme.typography.titleMedium
                )

                val heartRateString = if (healthData.heartRate == null) {
                    "/"
                } else {
                    "${healthData.heartRate}${stringResource(R.string.bpm)}"
                }

                val oxygenSaturationString = if (healthData.oxygenSaturation == null) {
                    "/"
                } else {
                    "${healthData.oxygenSaturation}${stringResource(R.string.percentage_sign)}"
                }

                Text(
                    text = stringResource(R.string.heart_rate_value, heartRateString),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = stringResource(
                        R.string.oxygen_saturation_value,
                        oxygenSaturationString
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
private fun HistoryListItemCardPreview() {
    HistoryListItemCard(
        healthData = HealthData(
            heartRate = 80,
            oxygenSaturation = 100,
            createdTime = "1.2.2025."
        )
    )
}