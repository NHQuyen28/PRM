package com.example.prm.utils

sealed class ResultState<T> {
    data class Success<T>(val data: T) : ResultState<T>()
    data class Error<T>(val message: String) : ResultState<T>()
    class Loading<T> : ResultState<T>()
}
