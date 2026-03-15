package com.example.prm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CartDataDto(

    @SerializedName("id")
    val id: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("items")
    val items: List<CartItemDto>,

    @SerializedName("subtotal")
    val subtotal: Double,

    @SerializedName("totalItems")
    val totalItems: Int
)

data class CartItemDto(

    @SerializedName("id")
    val id: String,

    @SerializedName("productVariantId")
    val productVariantId: String,

    @SerializedName("productId")
    val productId: String,

    @SerializedName("productName")
    val productName: String,

    @SerializedName("brandName")
    val brandName: String,

    @SerializedName("size")
    val size: String,

    @SerializedName("color")
    val color: String,

    @SerializedName("sku")
    val sku: String,

    @SerializedName("imageUrl")
    val imageUrl: String,

    @SerializedName("unitPrice")
    val unitPrice: Double,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("subtotal")
    val subtotal: Double,

    @SerializedName("stockQuantity")
    val stockQuantity: Int,

    @SerializedName("isAvailable")
    val isAvailable: Boolean
)