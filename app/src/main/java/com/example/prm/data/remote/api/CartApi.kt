package com.example.prm.data.remote.api

import com.example.prm.data.remote.dto.ApiResponse
import com.example.prm.data.remote.dto.UpdateCartRequest
import com.example.prm.data.remote.dto.cart.CartDataDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface CartApi {

    @GET("Cart")
    suspend fun getCart(): Response<ApiResponse<CartDataDto>>

    @PUT("Cart/update")
    suspend fun updateCart(
        @Body request: UpdateCartRequest
    ): Response<ApiResponse<String>>

}
