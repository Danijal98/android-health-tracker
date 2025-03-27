package com.danijalazerovic.android_health_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.danijalazerovic.android_health_tracker.ui.theme.AndroidHealthTrackerTheme
import com.danijalazerovic.android_health_tracker.ui.composables.App
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidHealthTrackerTheme {
                KoinAndroidContext {
                    App()
                }
            }
        }
    }
}