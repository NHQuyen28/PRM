package com.example.prm.data.repository

import com.example.prm.data.remote.api.AuthApi
import com.example.prm.data.remote.dto.BaseResponse
import com.example.prm.data.remote.dto.LoginRequest
import com.example.prm.data.remote.dto.RegisterRequest
import com.example.prm.utils.ResultState
import kotlinx.coroutines.delay

class AuthRepository {
    
    suspend fun login(email: String, password: String): ResultState<BaseResponse> {
        return try {
            delay(1000) // Simulate API call
            if (email.isNotEmpty() && password.isNotEmpty()) {
                ResultState.Success(
                    BaseResponse(
                        success = true,
                        message = "Login successful",
                        data = mapOf("token" to "mock_token_12345", "userId" to "1")
                    )
                )
            } else {
                ResultState.Error("Invalid credentials")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun register(
        fullName: String,
        email: String,
        password: String
    ): ResultState<BaseResponse> {
        return try {
            delay(1000)
            if (fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                ResultState.Success(
                    BaseResponse(
                        success = true,
                        message = "Registration successful",
                        data = mapOf("token" to "mock_token_12345", "userId" to "1")
                    )
                )
            } else {
                ResultState.Error("Please fill all fields")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error")
        }
    }
}




