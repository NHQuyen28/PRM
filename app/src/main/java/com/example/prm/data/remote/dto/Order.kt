package com.example.prm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("id")
    val id: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("orderCode")
    val orderCode: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("totalPrice")
    val totalPrice: Double,
    @SerializedName("shippingCost")
    val shippingCost: Double,
    @SerializedName("discountAmount")
    val discountAmount: Double,
    @SerializedName("finalPrice")
    val finalPrice: Double,
    @SerializedName("paymentMethod")
    val paymentMethod: String,
    @SerializedName("paymentStatus")
    val paymentStatus: String,
    @SerializedName("deliveryAddress")
    val deliveryAddress: Address,
    @SerializedName("orderItems")
    val orderItems: List<OrderItem>,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)

data class OrderItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("orderId")
    val orderId: String,
    @SerializedName("variantId")
    val variantId: String,
    @SerializedName("productId")
    val productId: String,
    @SerializedName("productName")
    val productName: String,
    @SerializedName("variantSku")
    val variantSku: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("price")
    val price: Double,
    @SerializedName("totalPrice")
    val totalPrice: Double,
    @SerializedName("imageUrl")
    val imageUrl: String? = null
)

data class Address(
    @SerializedName("id")
    val id: String,
    @SerializedName("recipientName")
    val recipientName: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("ward")
    val ward: String,
    @SerializedName("district")
    val district: String,
    @SerializedName("province")
    val province: String,
    @SerializedName("isDefault")
    val isDefault: Boolean
)
