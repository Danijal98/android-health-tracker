package com.danijalazerovic.android_health_tracker.domain.di

import androidx.room.Room
import com.danijalazerovic.android_health_tracker.data.database.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java, "database-name"
        ).build()
    }

    // DAOs
    single { get<AppDatabase>().getHistoryDao() }
}