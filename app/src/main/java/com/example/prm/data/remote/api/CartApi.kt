package com.example.prm.data.remote.api

import com.example.prm.data.remote.dto.ApiResponse
import com.example.prm.data.remote.dto.UpdateCartRequest
import com.example.prm.data.remote.dto.CartDataDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// ĐÃ SỬA: Đổi từ productId thành productVariantId để khớp 100% với Backend
data class AddToCartRequest(
    val productVariantId: String,
    val quantity: Int
)

interface CartApi {
    @GET("Cart")
    suspend fun getCart(): Response<ApiResponse<CartDataDto>>

    @POST("Cart/add")
    suspend fun addToCart(
        @Body request: AddToCartRequest
    ): Response<ApiResponse<CartDataDto>>

    @PUT("Cart/update")
    suspend fun updateCart(
        @Body request: UpdateCartRequest
    ): Response<ApiResponse<CartDataDto>>

    @DELETE("Cart/remove/{cartItemId}")
    suspend fun removeFromCart(
        @Path("cartItemId") cartItemId: String
    ): Response<ApiResponse<Boolean>>

    @DELETE("Cart/clear")
    suspend fun clearCart(): Response<ApiResponse<Boolean>>
}