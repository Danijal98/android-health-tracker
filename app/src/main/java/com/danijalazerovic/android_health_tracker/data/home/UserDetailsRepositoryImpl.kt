package com.danijalazerovic.android_health_tracker.data.home

import android.util.Log
import com.danijalazerovic.android_health_tracker.domain.Resource
import com.danijalazerovic.android_health_tracker.domain.contract.home.UserDetailsService
import com.danijalazerovic.android_health_tracker.domain.contract.home.UserDetailsRepository
import com.danijalazerovic.android_health_tracker.domain.model.UserDetails
import com.danijalazerovic.android_health_tracker.ui.viewmodel.state.home.HomeError

class UserDetailsRepositoryImpl(
    private val userDetailsService: UserDetailsService,
) : UserDetailsRepository {
    override suspend fun getUserDetails(): Resource<UserDetails> {
        return try {
            Resource.Success(userDetailsService.getUserDetails())
        } catch (e: Exception) {
            Log.e(UserDetailsRepositoryImpl::class.simpleName, "Get user details error", e)
            Resource.Error(HomeError.LoadingError)
        }
    }
}