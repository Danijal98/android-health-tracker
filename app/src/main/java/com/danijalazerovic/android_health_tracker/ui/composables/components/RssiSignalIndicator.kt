package com.danijalazerovic.android_health_tracker.ui.composables.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.danijalazerovic.android_health_tracker.R

@Composable
fun RssiSignalIndicator(modifier: Modifier = Modifier, rssi: Int) {
    val signalStrength = getSignalLevel(rssi)
    val signalImage = getRssiResource(signalStrength)

    Image(
        modifier = modifier,
        painter = painterResource(signalImage),
        contentDescription = null
    )
}

/**
 * Determines signal level based on RSSI value.
 */
private fun getSignalLevel(rssi: Int): Int {
    return when {
        rssi >= -50 -> 5 // Excellent signal (5 bars)
        rssi >= -60 -> 4 // Very good signal (4 bars)
        rssi >= -67 -> 3 // Good signal (3 bars)
        rssi >= -70 -> 2 // Fair signal (2 bars)
        rssi >= -80 -> 1 // Weak signal (1 bar)
        else -> 0  // No signal (0 bars)
    }
}

/**
 * Maps signal levels to respective SVG resources.
 */
private fun getRssiResource(level: Int): Int {
    return when (level) {
        5 -> R.drawable.signal_5
        4 -> R.drawable.signal_4
        3 -> R.drawable.signal_3
        2 -> R.drawable.signal_2
        1 -> R.drawable.signal_1
        else -> R.drawable.signal_unavailable
    }
}

@Preview
@Composable
private fun RssiSignalIndicatorPreview() {
    RssiSignalIndicator(rssi = -100)
}