package com.danijalazerovic.android_health_tracker.ui.composables.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.danijalazerovic.android_health_tracker.domain.model.HealthData

@Composable
fun DataCollectedCard(
    modifier: Modifier = Modifier,
    data: HealthData,
    isSaving: Boolean,
    onSave: (data: HealthData) -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth()
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
                    text = stringResource(R.string.heart_rate_value, "${data.heartRate}"),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.oxygen_saturation_value, "${data.oxygenSaturation}"),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            LoaderButton(
                text = stringResource(R.string.save),
                isLoading = isSaving,
                onClick = {
                    onSave(data)
                }
            )
        }
    }
}

@Preview
@Composable
private fun DataCollectedCardPreview() {
    DataCollectedCard(
        data = HealthData(
            heartRate = 80,
            oxygenSaturation = 100
        ),
        isSaving = false
    ) {}
}

@Preview
@Composable
private fun DataCollectedCardSavingPreview() {
    DataCollectedCard(
        data = HealthData(
            heartRate = 80,
            oxygenSaturation = 100
        ),
        isSaving = true
    ) {}
}