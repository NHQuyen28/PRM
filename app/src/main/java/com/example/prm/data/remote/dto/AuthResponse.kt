package com.example.prm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ApiResponseWrapper<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: T?
)

data class AuthResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("expiresAt")
    val expiresAt: String,
    @SerializedName("user")
    val user: UserInfo,
    @SerializedName("message")
    val message: String? = null
)

data class UserInfo(
    @SerializedName("id")
    val id: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("avatarUrl")
    val avatarUrl: String? = null,
    @SerializedName("roleDisplay")
    val role: String,
    @SerializedName("statusDisplay")
    val status: String
)
