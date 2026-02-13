package com.example.prm.ui.screens.cart

import com.example.prm.data.remote.dto.CartItem

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val subtotal: Double = 0.0,
    val discount: Double = 0.0,
    val voucherCode: String? = null,
    val total: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val voucherInput: String = ""
)
