package com.example.prm.data.repository

import com.example.prm.data.remote.api.AuthApi
import com.example.prm.data.remote.dto.LoginRequest
import com.example.prm.data.remote.dto.TokenData
import com.example.prm.utils.ResultState

class AuthRepository(
    private val api: AuthApi
) {

    suspend fun login(
        email: String,
        password: String
    ): ResultState<TokenData> {

        return try {
            val response = api.login(LoginRequest(email, password))

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.data != null) {
                    ResultState.Success(body.data)
                } else {
                    ResultState.Error("Invalid login response")
                }
            } else {
                ResultState.Error("Invalid email or password")
            }

        } catch (e: Exception) {
            ResultState.Error("Network error. Please try again")
        }
    }


}



