package com.example.prm.data.remote.api

import com.example.prm.data.remote.dto.ApiResponse
import com.example.prm.data.remote.dto.BrandResponse
import retrofit2.Response
import retrofit2.http.*

interface BrandApi {
    @GET("Brand")
    suspend fun getAllBrands(): Response<ApiResponse<BrandResponse>>

    @GET("Brand/{id}")
    suspend fun getBrandById(@Path("id") brandId: String): Response<ApiResponse<BrandItemResponse>>

    @POST("Brand")
    suspend fun createBrand(@Body request: CreateBrandRequest): Response<ApiResponse<BrandItemResponse>>

    @PUT("Brand/{id}")
    suspend fun updateBrand(
        @Path("id") brandId: String,
        @Body request: UpdateBrandRequest
    ): Response<ApiResponse<BrandItemResponse>>

    @DELETE("Brand/{id}")
    suspend fun deleteBrand(@Path("id") brandId: String): Response<ApiResponse<Unit>>
}

data class BrandItemResponse(
    val id: String,
    val brandName: String
)

data class CreateBrandRequest(
    val brandName: String,
    val description: String? = null
)

data class UpdateBrandRequest(
    val brandName: String,
    val description: String? = null
)
