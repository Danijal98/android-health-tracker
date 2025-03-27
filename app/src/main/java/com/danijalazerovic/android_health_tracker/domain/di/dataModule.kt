package com.danijalazerovic.android_health_tracker.domain.di

import com.danijalazerovic.android_health_tracker.data.bluetooth.BluetoothConnectionServiceImpl
import com.danijalazerovic.android_health_tracker.data.bluetooth.BluetoothRepositoryImpl
import com.danijalazerovic.android_health_tracker.data.bluetooth.BluetoothScanningServiceImpl
import com.danijalazerovic.android_health_tracker.data.history.HistoryRepositoryImpl
import com.danijalazerovic.android_health_tracker.data.home.UserDetailsServiceImpl
import com.danijalazerovic.android_health_tracker.domain.contract.bluetooth.BluetoothConnectionService
import com.danijalazerovic.android_health_tracker.domain.contract.bluetooth.BluetoothRepository
import com.danijalazerovic.android_health_tracker.domain.contract.bluetooth.BluetoothScanningService
import com.danijalazerovic.android_health_tracker.domain.contract.history.HistoryRepository
import com.danijalazerovic.android_health_tracker.domain.contract.home.UserDetailsRepository
import com.danijalazerovic.android_health_tracker.data.home.UserDetailsRepositoryImpl
import com.danijalazerovic.android_health_tracker.domain.contract.home.UserDetailsService
import org.koin.dsl.module

val dataModule = module {
    // Services
    single<BluetoothConnectionService> {
        BluetoothConnectionServiceImpl(get())
    }
    single<BluetoothScanningService> {
        BluetoothScanningServiceImpl(get())
    }
    single<UserDetailsService> { UserDetailsServiceImpl(get()) }

    // Repositories
    single<BluetoothRepository> {
        BluetoothRepositoryImpl(
            scanningService = get(),
            connectionService = get(),
            historyDao = get()
        )
    }
    single<HistoryRepository> { HistoryRepositoryImpl(get()) }
    single<UserDetailsRepository> { UserDetailsRepositoryImpl(get()) }
}