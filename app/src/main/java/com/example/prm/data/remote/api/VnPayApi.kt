package com.example.prm.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class CreateVnPayRequest(
    val orderId: String,
    val amount: Long,
    val orderDescription: String,
    val fullName: String
)

data class VnPayResponse(
    val paymentUrl: String
)

interface VnPayApi {

    @POST("payments/vnpay/create-payment")
    suspend fun createPayment(
        @Body request: CreateVnPayRequest
    ): Response<VnPayResponse>

}