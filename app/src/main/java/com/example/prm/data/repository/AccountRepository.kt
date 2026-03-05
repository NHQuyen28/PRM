package com.example.prm.data.repository

import com.example.prm.data.remote.RetrofitClient
import com.example.prm.data.remote.api.AccountApi
import com.example.prm.data.remote.dto.ProfileResponse
import com.example.prm.data.remote.dto.UpdateProfileRequest
import com.example.prm.utils.ResultState

class AccountRepository {

    private val api = RetrofitClient.createService(AccountApi::class.java)

    suspend fun getProfile(): ResultState<ProfileResponse> {
        return try {

            val response = api.getProfile()

            if (response.isSuccessful) {

                val body = response.body()

                if (body?.success == true && body.data != null) {
                    ResultState.Success(body.data)
                } else {
                    ResultState.Error(body?.message ?: "Unknown error")
                }

            } else {
                ResultState.Error("You are not logged in.")
            }

        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Network error")
        }
    }

    suspend fun updateProfile(
        request: UpdateProfileRequest
    ): ResultState<ProfileResponse> {

        return try {

            val response = api.updateProfile(request)

            if (response.isSuccessful) {

                val body = response.body()

                if (body?.success == true && body.data != null) {
                    ResultState.Success(body.data)
                } else {
                    ResultState.Error(body?.message ?: "Update failed")
                }

            } else {
                ResultState.Error("Update failed")
            }

        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Network error")
        }
    }

}