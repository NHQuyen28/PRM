package com.example.prm.data.repository

import com.example.prm.data.remote.RetrofitClient
import com.example.prm.data.remote.api.CreateMomoRequest
import com.example.prm.utils.ResultState

class PaymentRepository {

    private val api = RetrofitClient.paymentApi

    suspend fun createMomoPayment(
        orderId: String,
        amount: Double
    ): ResultState<String> {

        return try {

            val response = api.createMomoPayment(
                CreateMomoRequest(
                    orderId = orderId,
                    amount = amount.toLong(),
                    orderInfo = "Thanh toán đơn hàng",
                    extraData = ""
                )
            )

            if (response.isSuccessful) {

                val url = response.body()?.payUrl ?: ""

                ResultState.Success(url)

            } else {

                ResultState.Error("Create momo payment failed")

            }

        } catch (e: Exception) {

            ResultState.Error(e.message ?: "Payment error")

        }

    }

}