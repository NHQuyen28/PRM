package com.example.prm.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class CreateMomoRequest(
    val orderId: String,
    val amount: Long,
    val orderInfo: String,
    val extraData: String = ""
)

data class MomoResponse(
    val payUrl: String
)

interface PaymentApi {

    @POST("payments/momo/create")
    suspend fun createMomoPayment(
        @Body request: CreateMomoRequest
    ): Response<MomoResponse>

}