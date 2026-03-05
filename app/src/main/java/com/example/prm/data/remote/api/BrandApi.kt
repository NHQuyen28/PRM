package com.example.prm.data.remote.api

import com.example.prm.data.remote.dto.ApiResponse
import com.example.prm.data.remote.dto.BrandResponse
import retrofit2.Response
import retrofit2.http.GET

interface BrandApi {
    @GET("Brand")
    suspend fun getAllBrands(): Response<ApiResponse<BrandResponse>>
}
