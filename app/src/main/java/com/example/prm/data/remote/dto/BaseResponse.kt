package com.example.prm.data.remote.dto

data class BaseResponse<T>(
    val status: Int,
    val message: String,
    val data: T?
)