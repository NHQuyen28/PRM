package com.example.prm.ui.screens.cart

import com.example.prm.data.remote.dto.cart.CartDataDto

data class CartUiState(

    val isLoading: Boolean = false,

    val cart: CartDataDto? = null,

    val error: String? = null
)