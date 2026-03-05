package com.example.prm.data.repository

import com.example.prm.data.remote.RetrofitClient
import com.example.prm.data.remote.api.CartApi
import com.example.prm.data.remote.dto.UpdateCartRequest
import com.example.prm.data.remote.dto.cart.CartDataDto
import com.example.prm.utils.ResultState

class CartRepository {

    private val api = RetrofitClient.createService(CartApi::class.java)

    suspend fun getCart(): ResultState<CartDataDto> {
        return try {

            val response = api.getCart()

            if (response.isSuccessful) {

                val body = response.body()

                if (body?.success == true && body.data != null) {
                    ResultState.Success(body.data)
                } else {
                    ResultState.Error(body?.message ?: "Failed to load cart")
                }

            } else {
                ResultState.Error("API Error ${response.code()}")
            }

        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun updateCart(
        cartItemId: String,
        quantity: Int
    ): ResultState<String> {

        return try {

            val response = api.updateCart(
                UpdateCartRequest(cartItemId, quantity)
            )

            if (response.isSuccessful && response.body() != null) {

                val apiResponse = response.body()!!

                if (apiResponse.success) {
                    ResultState.Success("Updated")
                } else {
                    ResultState.Error(apiResponse.message ?: "Update failed")
                }

            } else {
                ResultState.Error("HTTP ${response.code()}")
            }

        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Update cart error")
        }
    }
}