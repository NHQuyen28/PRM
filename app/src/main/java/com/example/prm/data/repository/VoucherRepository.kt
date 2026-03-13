package com.example.prm.data.repository

import android.util.Log
import com.example.prm.data.remote.RetrofitClient
import com.example.prm.data.remote.api.VoucherApi
import com.example.prm.data.remote.dto.*
import com.example.prm.utils.ResultState
import com.google.gson.Gson

class VoucherRepository {
    private val voucherApi = RetrofitClient.createService(VoucherApi::class.java)
    private val gson = Gson()
    private val TAG = "VoucherRepository"

    suspend fun getAllVouchers(page: Int = 1, pageSize: Int = 50): ResultState<VoucherListResp> {
        return try {
            val response = voucherApi.getAllVouchers(page, pageSize)
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    ResultState.Success(apiResponse.data)
                } else {
                    ResultState.Error(apiResponse.message ?: "Failed to fetch vouchers")
                }
            } else {
                ResultState.Error("HTTP ${response.code()}: Failed to fetch vouchers")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception fetching vouchers", e)
            ResultState.Error(e.message ?: "Connection error")
        }
    }

    suspend fun getVoucherById(id: String): ResultState<VoucherResp> {
        return try {
            val response = voucherApi.getVoucherById(id)
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    ResultState.Success(apiResponse.data)
                } else {
                    ResultState.Error(apiResponse.message ?: "Failed to fetch voucher details")
                }
            } else {
                ResultState.Error("HTTP ${response.code()}: Failed to fetch voucher details")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception fetching voucher details", e)
            ResultState.Error(e.message ?: "Connection error")
        }
    }

    suspend fun createVoucher(request: CreateVoucherReq): ResultState<VoucherResp> {
        return try {
            val response = voucherApi.createVoucher(request)
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    ResultState.Success(apiResponse.data)
                } else {
                    ResultState.Error(apiResponse.message ?: "Failed to create voucher")
                }
            } else {
                var errorMessage = "HTTP ${response.code()}: Failed to create voucher"
                try {
                    val errorBody = response.errorBody()?.string()
                    if (errorBody != null) {
                        val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                        if (errorResponse.message != null) {
                            errorMessage = errorResponse.message
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing error response", e)
                }
                ResultState.Error(errorMessage)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception creating voucher", e)
            ResultState.Error(e.message ?: "Connection error")
        }
    }

    suspend fun updateVoucher(request: UpdateVoucherReq): ResultState<VoucherResp> {
        return try {
            val response = voucherApi.updateVoucher(request)
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    ResultState.Success(apiResponse.data)
                } else {
                    ResultState.Error(apiResponse.message ?: "Failed to update voucher")
                }
            } else {
                var errorMessage = "HTTP ${response.code()}: Failed to update voucher"
                try {
                    val errorBody = response.errorBody()?.string()
                    if (errorBody != null) {
                        val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                        if (errorResponse.message != null) {
                            errorMessage = errorResponse.message
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing error response", e)
                }
                ResultState.Error(errorMessage)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception updating voucher", e)
            ResultState.Error(e.message ?: "Connection error")
        }
    }

    suspend fun deleteVoucher(id: String): ResultState<Boolean> {
        return try {
            val response = voucherApi.deleteVoucher(id)
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success) {
                    ResultState.Success(apiResponse.data ?: true)
                } else {
                    ResultState.Error(apiResponse.message ?: "Failed to delete voucher")
                }
            } else {
                ResultState.Error("HTTP ${response.code()}: Failed to delete voucher")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception deleting voucher", e)
            ResultState.Error(e.message ?: "Connection error")
        }
    }
}