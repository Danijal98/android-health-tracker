package com.danijalazerovic.android_health_tracker.data.home

import com.danijalazerovic.android_health_tracker.domain.contract.home.UserDetailsService
import com.danijalazerovic.android_health_tracker.domain.model.UserDetails
import io.ktor.client.HttpClient
import kotlinx.coroutines.delay

class UserDetailsServiceImpl(
    private val httpClient: HttpClient
) : UserDetailsService {
    override suspend fun getUserDetails(): UserDetails {
        // TODO: Remove when calling with http client
        delay(1000)
        // TODO: commented out not to waste api mocha calls
//        return httpClient
//            .get("https://apimocha.com/healthtracker/userDetails")
//            .body<UserDetails>()

        return UserDetails(
            averageHeartRate = (75..120).random(),
            averageOxygenSaturation = (50..100).random()
        )
    }
}