package com.example.prm.ui.screens.product_detail

import com.example.prm.data.remote.dto.ProductDetail

data class ProductDetailUiState(
    val product: ProductDetail? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedVariantId: Int? = null,
    val selectedAddons: List<Int> = emptyList(),
    val quantity: Int = 1
)
