package com.example.prm.data.remote.dto

data class UpdateProfileRequest(
    val fullName: String,
    val phone: String?,
    val address: String?,
    val avatarUrl: String?
)