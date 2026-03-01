package com.example.prm.data.repository

import com.example.prm.data.remote.api.AuthApi
import com.example.prm.data.remote.dto.AuthResponse
import com.example.prm.data.remote.dto.LoginRequest
import com.example.prm.data.remote.dto.RegisterRequest
import com.example.prm.data.remote.RetrofitClient
import com.example.prm.utils.ResultState
import com.google.gson.Gson

class AuthRepository {
    private val authApi = RetrofitClient.createService(AuthApi::class.java)
    private val gson = Gson()

    suspend fun login(email: String, password: String): ResultState<AuthResponse> {
        return try {
            val request = LoginRequest(email = email, password = password)
            val response = authApi.login(request)

            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    ResultState.Success(apiResponse.data)
                } else {
                    ResultState.Error(apiResponse.message ?: "Login failed")
                }
            } else {
                // Try to parse error response body
                var errorMessage = "HTTP ${response.code()}: Login failed"
                try {
                    val errorBody = response.errorBody()?.string()
                    if (errorBody != null) {
                        val errorResponse = gson.fromJson(errorBody, com.example.prm.data.remote.dto.ApiResponse::class.java)
                        if (errorResponse.message != null) {
                            errorMessage = errorResponse.message
                        }
                    }
                } catch (e: Exception) {
                    // Ignore parsing errors
                }
                ResultState.Error(errorMessage)
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Connection error")
        }
    }

    suspend fun register(
        fullName: String,
        email: String,
        password: String,
        phone: String? = null
    ): ResultState<AuthResponse> {
        return try {
            val request = RegisterRequest(
                email = email,
                password = password,
                confirmPassword = password,
                fullName = fullName,
                phone = phone
            )
            val response = authApi.register(request)

            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    ResultState.Success(apiResponse.data)
                } else {
                    ResultState.Error(apiResponse.message ?: "Registration failed")
                }
            } else {
                // Try to parse error response body
                var errorMessage = "HTTP ${response.code()}: Registration failed"
                try {
                    val errorBody = response.errorBody()?.string()
                    if (errorBody != null) {
                        val errorResponse = gson.fromJson(errorBody, com.example.prm.data.remote.dto.ApiResponse::class.java)
                        if (errorResponse.message != null) {
                            errorMessage = errorResponse.message
                        }
                    }
                } catch (e: Exception) {
                    // Ignore parsing errors
                }
                ResultState.Error(errorMessage)
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Connection error")
        }
    }
}




