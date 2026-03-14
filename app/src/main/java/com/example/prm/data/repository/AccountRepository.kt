package com.example.prm.data.repository

import com.example.prm.data.remote.RetrofitClient
import com.example.prm.data.remote.api.AccountApi
import com.example.prm.data.remote.dto.ProfileResponse
import com.example.prm.data.remote.dto.UpdateProfileRequest
import com.example.prm.utils.ResultState
import android.util.Log

class AccountRepository {
    companion object {
        private const val TAG = "AccountRepository"
    }

    private val api = RetrofitClient.createService(AccountApi::class.java)

    suspend fun getProfile(): ResultState<ProfileResponse> {
        return try {

            Log.d(TAG, "getProfile: calling API...")
            val response = api.getProfile()
            Log.d(TAG, "getProfile: response code = ${response.code()}, isSuccessful = ${response.isSuccessful}")

            if (response.isSuccessful) {

                val body = response.body()
                Log.d(TAG, "getProfile: body = $body")

                if (body?.success == true && body.data != null) {
                    Log.d(TAG, "getProfile: success, got profile")
                    ResultState.Success(body.data)
                } else {
                    Log.e(TAG, "getProfile: body failed, message = ${body?.message}")
                    ResultState.Error(body?.message ?: "Unknown error")
                }

            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "getProfile: failed, code = ${response.code()}, error = $errorBody")
                ResultState.Error("You are not logged in.")
            }

        } catch (e: Exception) {
            Log.e(TAG, "getProfile: exception = ${e.message}", e)
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