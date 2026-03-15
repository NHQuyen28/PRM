package com.example.prm.data.repository

import android.util.Log
import com.example.prm.data.remote.RetrofitClient
import com.example.prm.data.remote.api.AddToCartRequest
import com.example.prm.data.remote.api.CartApi
import com.example.prm.data.remote.dto.CartDataDto
import com.example.prm.data.remote.dto.UpdateCartRequest
import com.example.prm.utils.ResultState

class CartRepository {
    private val cartApi = RetrofitClient.createService(CartApi::class.java)
    private val TAG = "CartRepository"

    suspend fun getCart(): ResultState<CartDataDto> {
        return try {
            val response = cartApi.getCart()
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    ResultState.Success(apiResponse.data)
                } else {
                    ResultState.Error(apiResponse.message ?: "Failed to get cart")
                }
            } else {
                ResultState.Error("HTTP Error ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception getting cart", e)
            ResultState.Error(e.message ?: "Network error")
        }
    }

    suspend fun addToCart(productVariantId: String, quantity: Int): ResultState<String> {
        return try {
            val request = AddToCartRequest(productVariantId, quantity)
            val response = cartApi.addToCart(request)
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success) {
                    ResultState.Success(apiResponse.message ?: "Added to cart successfully")
                } else {
                    ResultState.Error(apiResponse.message ?: "Failed to add to cart")
                }
            } else {
                ResultState.Error("HTTP Error ${response.code()} (Check Token)")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception adding to cart", e)
            ResultState.Error(e.message ?: "Network error")
        }
    }

    suspend fun updateCartItem(cartItemId: String, quantity: Int): ResultState<String> {
        return try {
            val request = UpdateCartRequest(cartItemId, quantity)
            val response = cartApi.updateCart(request)
            if (response.isSuccessful && response.body() != null) {
                ResultState.Success(response.body()!!.message ?: "Updated successfully")
            } else {
                ResultState.Error("HTTP Error ${response.code()}")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Network error")
        }
    }

    suspend fun removeFromCart(cartItemId: String): ResultState<String> {
        return try {
            val response = cartApi.removeFromCart(cartItemId)
            if (response.isSuccessful && response.body() != null) {
                ResultState.Success(response.body()!!.message ?: "Removed successfully")
            } else {
                ResultState.Error("HTTP Error ${response.code()}")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Network error")
        }
    }

    suspend fun clearCart(): ResultState<String> {
        return try {
            val response = cartApi.clearCart()
            if (response.isSuccessful && response.body() != null) {
                ResultState.Success(response.body()!!.message ?: "Cleared successfully")
            } else {
                ResultState.Error("HTTP Error ${response.code()}")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Network error")
        }
    }
}