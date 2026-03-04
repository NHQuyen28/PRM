package com.example.prm.data.remote.api

import com.example.prm.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ProductApi {
    @GET("Product")
    suspend fun getProducts(
        @Query("Page") page: Int = 1,
        @Query("PageSize") pageSize: Int = 20
    ): Response<ApiResponse<ProductListResponse>>
}
