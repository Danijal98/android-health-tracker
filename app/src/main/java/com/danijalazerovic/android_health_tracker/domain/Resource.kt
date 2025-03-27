package com.danijalazerovic.android_health_tracker.domain

sealed class Resource<out T> {
    data class Success<out T>(val data: T): Resource<T>()
    data class Error(val exception: Exception): Resource<Nothing>()
}

fun <T, K> Resource<T>.map(mapper: (T) -> K): Resource<K> = when (this) {
    is Resource.Success -> Resource.Success(mapper(data))
    is Resource.Error -> Resource.Error(exception)
}