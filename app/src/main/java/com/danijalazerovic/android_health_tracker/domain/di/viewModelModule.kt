package com.danijalazerovic.android_health_tracker.domain.di

import com.danijalazerovic.android_health_tracker.ui.viewmodel.BluetoothScanningViewModel
import com.danijalazerovic.android_health_tracker.ui.viewmodel.HomeViewModel
import com.danijalazerovic.android_health_tracker.ui.viewmodel.BluetoothCollectionViewModel
import com.danijalazerovic.android_health_tracker.ui.viewmodel.HistoryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::BluetoothScanningViewModel)
    viewModelOf(::BluetoothCollectionViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::HistoryViewModel)
}