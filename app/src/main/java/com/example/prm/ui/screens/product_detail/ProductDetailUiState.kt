package com.example.prm.ui.screens.product_detail

import com.example.prm.data.remote.dto.ProductResp

data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val product: ProductResp? = null, // <<< Sử dụng ProductResp thật
    val errorMessage: String? = null,
    val isAddingToCart: Boolean = false,
    val addToCartSuccess: Boolean = false
)