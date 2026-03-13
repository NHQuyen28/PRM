package com.example.prm.data.remote.dto

data class CreateAddressRequest(
    val id: String?,
    val recipientName: String,
    val phone: String,
    val province: String,
    val district: String,
    val ward: String,
    val detailAddress: String,
    val isDefault: Boolean
)