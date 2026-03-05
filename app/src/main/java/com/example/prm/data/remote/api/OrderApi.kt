package com.example.prm.data.remote.api

import com.example.prm.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface OrderApi {
    @GET("Order")
    suspend fun getUserOrders(
        @Query("pageNumber") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): Response<ApiResponse<OrderListResponse>>

    @GET("Order/{id}")
    suspend fun getOrderById(@Path("id") orderId: String): Response<ApiResponse<OrderDetailResponse>>

    @POST("Order")
    suspend fun createOrder(@Body request: CreateOrderRequest): Response<ApiResponse<OrderDetailResponse>>

    @PUT("Order/status")
    suspend fun updateOrderStatus(@Body request: UpdateOrderStatusRequest): Response<ApiResponse<OrderDetailResponse>>

    @POST("Order/cancel")
    suspend fun cancelOrder(@Body request: CancelOrderRequest): Response<ApiResponse<OrderDetailResponse>>
}

data class CreateOrderRequest(
    val deliveryAddressId: String,
    val shippingMethod: String,
    val paymentMethod: String,
    val voucherId: String? = null,
    val notes: String? = null
)

data class UpdateOrderStatusRequest(
    val orderId: String,
    val status: String
)

data class CancelOrderRequest(
    val orderId: String,
    val reason: String? = null
)

data class OrderListResponse(
    val orders: List<OrderResponse>,
    val pagination: PaginationResp
)

data class OrderResponse(
    val id: String,
    val userId: String,
    val orderCode: String,
    val status: String,
    val totalPrice: Double,
    val shippingCost: Double,
    val discountAmount: Double,
    val finalPrice: Double,
    val paymentMethod: String,
    val paymentStatus: String,
    val deliveryAddress: AddressResponse,
    val orderItems: List<OrderItemResponse>,
    val createdAt: String,
    val updatedAt: String
)

data class OrderDetailResponse(
    val id: String,
    val userId: String,
    val orderCode: String,
    val status: String,
    val totalPrice: Double,
    val shippingCost: Double,
    val discountAmount: Double,
    val finalPrice: Double,
    val paymentMethod: String,
    val paymentStatus: String,
    val deliveryAddress: AddressResponse,
    val orderItems: List<OrderItemResponse>,
    val timeline: List<OrderTimelineResponse>?,
    val createdAt: String,
    val updatedAt: String
)

data class OrderItemResponse(
    val id: String,
    val orderId: String,
    val variantId: String,
    val productId: String,
    val productName: String,
    val variantSku: String,
    val quantity: Int,
    val price: Double,
    val totalPrice: Double,
    val imageUrl: String?
)

data class OrderTimelineResponse(
    val status: String,
    val timestamp: String,
    val notes: String?
)

data class AddressResponse(
    val id: String,
    val recipientName: String,
    val phoneNumber: String,
    val address: String,
    val ward: String,
    val district: String,
    val province: String,
    val isDefault: Boolean
)
