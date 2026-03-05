package com.example.prm.data.remote.dto

data class UpdateCartRequest(
    val cartItemId: String,
    val quantity: Int
)