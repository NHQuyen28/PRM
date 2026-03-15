package com.example.prm.data.remote.api

import com.example.prm.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface OrderApi {

//    @POST("Order")
//    suspend fun createOrder(
//        @Body request: CreateOrderRequest
//    ): Response<OrderResponse>

    @POST("Order")
    suspend fun createOrder(
        @Body request: CreateOrderRequest
    ): Response<ApiResponse<OrderResponse>>

    @GET("order")
    suspend fun getOrders(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Response<OrderListResponse>

    @GET("order/{id}")
    suspend fun getOrderDetail(
        @Path("id") orderId: String
    ): Response<OrderResponse>

}


data class UpdateOrderStatusRequest(
    val orderId: String,
    val status: String
)

data class CancelOrderRequest(
    val orderId: String,
    val reason: String? = null
)


