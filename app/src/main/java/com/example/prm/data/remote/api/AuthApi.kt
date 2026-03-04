package com.example.prm.data.remote.api

import com.example.prm.data.remote.dto.ApiResponse
import com.example.prm.data.remote.dto.AuthResponse
import com.example.prm.data.remote.dto.LoginRequest
import com.example.prm.data.remote.dto.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("Auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<AuthResponse>>

    @POST("Auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<AuthResponse>>
}
