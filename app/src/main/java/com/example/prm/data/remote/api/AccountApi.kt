package com.example.prm.data.remote.api

import com.example.prm.data.remote.dto.ApiResponse
import com.example.prm.data.remote.dto.ProfileResponse
import com.example.prm.data.remote.dto.UpdateProfileRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface AccountApi {

    @GET("Account/profile")
    suspend fun getProfile(): Response<ApiResponse<ProfileResponse>>

    @PUT("Account/profile")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequest
    ): Response<ApiResponse<ProfileResponse>>

}