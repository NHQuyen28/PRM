package com.example.prm.data.remote.dto

data class ProfileResponse(
    val id: String,
    val email: String,
    val fullName: String,
    val phone: String?,
    val address: String?,
    val avatarUrl: String?,
    val role: Int,
    val roleDisplay: String,
    val status: Int,
    val statusDisplay: String,
    val createdAt: String
)