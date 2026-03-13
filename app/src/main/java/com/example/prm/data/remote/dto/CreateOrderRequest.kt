package com.example.prm.data.remote.dto

data class CreateOrderRequest(

    val addressId: String,

    val recipientName: String,

    val recipientPhone: String,

    val shippingAddress: String,

    val voucherId: String?,

    val paymentMethod: Int,

    val notes: String?

)