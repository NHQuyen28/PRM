package com.example.prm.data.remote.api

import com.example.prm.data.remote.dto.ApiResponse
import com.example.prm.data.remote.dto.CategoryResponse
import retrofit2.Response
import retrofit2.http.*

interface CategoryApi {
    @GET("Category")
    suspend fun getAllCategories(): Response<ApiResponse<CategoryResponse>>

    @GET("Category/{id}")
    suspend fun getCategoryById(@Path("id") categoryId: String): Response<ApiResponse<CategoryItemResponse>>

    @POST("Category")
    suspend fun createCategory(@Body request: CreateCategoryRequest): Response<ApiResponse<CategoryItemResponse>>

    @PUT("Category/{id}")
    suspend fun updateCategory(
        @Path("id") categoryId: String,
        @Body request: UpdateCategoryRequest
    ): Response<ApiResponse<CategoryItemResponse>>

    @DELETE("Category/{id}")
    suspend fun deleteCategory(@Path("id") categoryId: String): Response<ApiResponse<Unit>>
}

data class CategoryItemResponse(
    val id: String,
    val categoryName: String
)

data class CreateCategoryRequest(
    val categoryName: String,
    val description: String? = null
)

data class UpdateCategoryRequest(
    val categoryName: String,
    val description: String? = null
)
