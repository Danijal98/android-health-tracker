package com.danijalazerovic.android_health_tracker.domain.contract.home

import com.danijalazerovic.android_health_tracker.domain.Resource
import com.danijalazerovic.android_health_tracker.domain.model.UserDetails

interface UserDetailsRepository {
    suspend fun getUserDetails(): Resource<UserDetails>
}