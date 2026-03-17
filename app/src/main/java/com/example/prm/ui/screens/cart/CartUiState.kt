package com.example.prm.ui.screens.cart

import com.example.prm.data.remote.dto.CartDataDto
import com.example.prm.data.remote.dto.VoucherResp

data class CartUiState(

    val isLoading: Boolean = false,

    val cart: CartDataDto? = null,

    val error: String? = null,

    val selectedVoucher: VoucherResp? = null,

    val discountAmount: Double = 0.0
)