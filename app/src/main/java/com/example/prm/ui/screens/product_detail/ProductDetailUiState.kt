package com.example.prm.ui.screens.product_detail

import com.example.prm.data.remote.dto.ProductResp
import com.example.prm.data.remote.dto.ProductVariantResp

data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val product: ProductResp? = null,
    val selectedVariant: ProductVariantResp? = null, // <<< ĐÃ THÊM: Lưu biến thể đang chọn
    val errorMessage: String? = null,
    val isAddingToCart: Boolean = false,
    val addToCartSuccess: Boolean = false
)