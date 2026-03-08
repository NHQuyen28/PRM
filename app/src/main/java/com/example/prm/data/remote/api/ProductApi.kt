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

    @GET("Product/{id}")
    suspend fun getProductById(@Path("id") productId: String): Response<ApiResponse<ProductResp>>

    @GET("Product/slug/{slug}")
    suspend fun getProductBySlug(@Path("slug") slug: String): Response<ApiResponse<ProductResp>>

    @GET("Product/search")
    suspend fun searchProducts(
        @Query("keyword") keyword: String? = null,
        @Query("categoryId") categoryId: String? = null,
        @Query("brandId") brandId: String? = null,
        @Query("minPrice") minPrice: Double? = null,
        @Query("maxPrice") maxPrice: Double? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("Page") page: Int = 1,
        @Query("PageSize") pageSize: Int = 20
    ): Response<ApiResponse<ProductListResponse>>

    @GET("Product/category/{categoryId}")
    suspend fun getProductsByCategory(
        @Path("categoryId") categoryId: String,
        @Query("Page") page: Int = 1,
        @Query("PageSize") pageSize: Int = 20
    ): Response<ApiResponse<ProductListResponse>>

    @GET("Product/brand/{brandId}")
    suspend fun getProductsByBrand(
        @Path("brandId") brandId: String,
        @Query("Page") page: Int = 1,
        @Query("PageSize") pageSize: Int = 20
    ): Response<ApiResponse<ProductListResponse>>

    @POST("Product")
    suspend fun createProduct(@Body request: CreateProductRequest): Response<ApiResponse<ProductResp>>

    @PUT("Product")
    suspend fun updateProduct(@Body request: UpdateProductRequest): Response<ApiResponse<ProductResp>>

    @DELETE("Product/{id}")
    suspend fun deleteProduct(@Path("id") productId: String): Response<ApiResponse<Unit>>

    @POST("Product/{productId}/variant")
    suspend fun addProductVariant(
        @Path("productId") productId: String,
        @Body request: CreateProductVariantRequest
    ): Response<ApiResponse<ProductVariantResp>>
}

data class CreateProductRequest(
    val categoryId: String,
    val brandId: String,
    val productName: String,
    val slug: String? = null,
    val description: String? = null,
    val basePrice: Double,
    val isActive: Boolean = true,
    val variants: List<CreateProductVariantRequest>? = null,
    val imageUrls: List<String>? = null
)

data class UpdateProductRequest(
    val id: String,
    val categoryId: String,
    val brandId: String,
    val productName: String,
    val slug: String? = null,
    val description: String? = null,
    val basePrice: Double,
    val isActive: Boolean,
    val imageUrls: List<String>? = null
)

data class CreateProductVariantRequest(
    val sku: String,
    val size: String? = null,
    val color: String? = null,
    val weight: String? = null,
    val gripSize: String? = null,
    val stringTension: String? = null,
    val speed: String? = null,
    val material: String? = null,
    val priceAdjustment: Double = 0.0,
    val stockQuantity: Int,
    val isActive: Boolean = true
)
