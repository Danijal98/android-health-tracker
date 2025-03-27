package com.danijalazerovic.android_health_tracker.domain.contract.home

import com.danijalazerovic.android_health_tracker.domain.model.UserDetails

interface UserDetailsService {
    suspend fun getUserDetails(): UserDetails
}