package com.example.prm.data.remote.dto

data class OrderListResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val data: OrderData
)

data class OrderData(
    val orders: List<OrderResponse>,
    val pagination: Pagination
)

data class Pagination(
    val page: Int,
    val pageSize: Int,
    val total: Int
)