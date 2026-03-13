package com.example.prm.data.remote.dto

data class AddressResponse(
    val id: String,
    val recipientName: String,
    val phone: String,
    val province: String,
    val district: String,
    val ward: String,
    val detailAddress: String,
    val fullAddress: String,
    val isDefault: Boolean
)

data class AddressListResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val data: List<AddressResponse>
)