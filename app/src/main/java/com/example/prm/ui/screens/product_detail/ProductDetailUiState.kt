package com.example.prm.ui.screens.product_detail

import com.example.prm.data.remote.dto.ProductDetail

data class ProductDetailUiState(
    val product: ProductDetail? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedVariantId: String? = null,
    val selectedAddons: List<String> = emptyList(),
    val quantity: Int = 1
)
