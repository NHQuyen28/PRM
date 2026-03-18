package com.example.prm.data.repository

import android.util.Log
import com.example.prm.data.remote.RetrofitClient
import com.example.prm.data.remote.api.CreateVnPayRequest
import com.example.prm.utils.ResultState

class VnPayRepository {

    private val api = RetrofitClient.vnPayApi

    suspend fun createPayment(
        orderId: String,
        amount: Double,
        fullName: String
    ): ResultState<String> {

        return try {

            val request = CreateVnPayRequest(
                orderId = orderId,
                amount = (amount).toLong(),
                orderDescription = "Thanh toán đơn hàng",
                fullName = fullName
            )

            val response = api.createPayment(request)

            Log.d("VNPAY_DEBUG", "code = ${response.code()}")
            Log.d("VNPAY_DEBUG", "body = ${response.body()}")

            if (response.isSuccessful) {

                val url = response.body()?.paymentUrl ?: ""

                ResultState.Success(url)

            } else {

                ResultState.Error("VNPay failed")

            }

        } catch (e: Exception) {

            ResultState.Error(e.message ?: "VNPay error")

        }
    }
}