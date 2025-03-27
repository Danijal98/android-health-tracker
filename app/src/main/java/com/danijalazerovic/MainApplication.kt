package com.danijalazerovic

import android.app.Application
import com.danijalazerovic.android_health_tracker.domain.di.coreModule
import com.danijalazerovic.android_health_tracker.domain.di.dataModule
import com.danijalazerovic.android_health_tracker.domain.di.databaseModule
import com.danijalazerovic.android_health_tracker.domain.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(coreModule, databaseModule, dataModule, viewModelModule)
        }
    }

}