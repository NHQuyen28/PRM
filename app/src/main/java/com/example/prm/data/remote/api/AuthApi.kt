package com.example.prm.data.remote.api

import com.example.prm.data.remote.dto.LoginRequest
import com.example.prm.data.remote.dto.TokenData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T?
)

interface AuthApi {

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ApiResponse<TokenData>>


}