package com.example.prm.data.remote.dto

data class OrderResponse(

    val id: String,
    val orderNumber: String,
    val status: Int,
    val statusDisplay: String,

    val recipientName: String,
    val recipientPhone: String,
    val shippingAddress: String,

    val subtotal: Double,
    val shippingFee: Double,
    val discountAmount: Double,
    val totalAmount: Double,

    val paymentMethodDisplay: String,
    val paymentStatusDisplay: String,

    val createdAt: String,

    val orderDetails: List<OrderDetailResponse>
)

data class OrderDetailResponse(

    val id: String,
    val productName: String,
    val variantInfo: String,
    val unitPrice: Double,
    val quantity: Int,
    val subtotal: Double
)