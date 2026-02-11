package com.example.prm.data.remote.api

import com.example.prm.data.remote.dto.BaseResponse
import com.example.prm.data.remote.dto.LoginRequest
import com.example.prm.data.remote.dto.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<BaseResponse>

    @POST("/api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<BaseResponse>
}
