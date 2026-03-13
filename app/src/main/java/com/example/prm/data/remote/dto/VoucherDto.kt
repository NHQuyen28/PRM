package com.example.prm.data.remote.dto

data class VoucherResp(
    val id: String,
    val code: String,
    val name: String,
    val description: String?,
    val discountAmount: Double,
    val discountPercentage: Double,
    val minimumOrderAmount: Double,
    val maximumDiscountAmount: Double,
    val usageLimit: Int,
    val usedCount: Int,
    val startDate: String, // Trả về dạng ISO string từ Backend
    val endDate: String,
    val isActive: Boolean,
    val isValid: Boolean,
    val invalidReason: String?
)

data class VoucherListResp(
    val vouchers: List<VoucherResp> = emptyList(),
    val pagination: PaginationResp? = null // Tận dụng PaginationResp đã có sẵn của bạn
)

data class CreateVoucherReq(
    val code: String,
    val name: String,
    val description: String?,
    val discountAmount: Double,
    val discountPercentage: Double,
    val minimumOrderAmount: Double,
    val maximumDiscountAmount: Double,
    val usageLimit: Int,
    val startDate: String,
    val endDate: String,
    val isActive: Boolean
)

data class UpdateVoucherReq(
    val id: String,
    val code: String,
    val name: String,
    val description: String?,
    val discountAmount: Double,
    val discountPercentage: Double,
    val minimumOrderAmount: Double,
    val maximumDiscountAmount: Double,
    val usageLimit: Int,
    val startDate: String,
    val endDate: String,
    val isActive: Boolean
)