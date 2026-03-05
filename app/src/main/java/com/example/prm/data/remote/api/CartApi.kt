package com.example.prm.data.remote.api

import com.example.prm.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface CartApi {
    @GET("Cart")
    suspend fun getCart(): Response<ApiResponse<CartResponse>>

    @POST("Cart/add")
    suspend fun addToCart(@Body request: AddToCartRequest): Response<ApiResponse<CartResponse>>

    @PUT("Cart/update")
    suspend fun updateCartItem(@Body request: UpdateCartItemRequest): Response<ApiResponse<CartResponse>>

    @DELETE("Cart/remove/{cartItemId}")
    suspend fun removeCartItem(@Path("cartItemId") cartItemId: String): Response<ApiResponse<CartResponse>>

    @DELETE("Cart/clear")
    suspend fun clearCart(): Response<ApiResponse<Unit>>
}

data class AddToCartRequest(
    val variantId: String,
    val quantity: Int
)

data class UpdateCartItemRequest(
    val cartItemId: String,
    val quantity: Int
)

data class CartResponse(
    val id: String,
    val userId: String,
    val items: List<CartItemResponse>,
    val totalPrice: Double,
    val totalItems: Int,
    val updatedAt: String
)

data class CartItemResponse(
    val id: String,
    val cartId: String,
    val variantId: String,
    val productId: String,
    val productName: String,
    val variantSku: String,
    val quantity: Int,
    val price: Double,
    val totalPrice: Double,
    val imageUrl: String?
)
