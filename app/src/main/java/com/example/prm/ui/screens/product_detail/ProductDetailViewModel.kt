package com.example.prm.ui.screens.product_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.remote.dto.ProductVariantResp
import com.example.prm.data.repository.CartRepository
import com.example.prm.data.repository.ProductRepository
import com.example.prm.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailViewModel : ViewModel() {
    private val productRepository = ProductRepository()
    private val cartRepository = CartRepository() // <<< ĐÃ THÊM: Sử dụng đúng CartRepository

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = productRepository.getProductById(productId)) {
                is ResultState.Success -> {
                    val product = result.data
                    val defaultVariant = product.variants?.firstOrNull { it.isActive }

                    _uiState.update {
                        it.copy(isLoading = false, product = product, selectedVariant = defaultVariant)
                    }
                }
                is ResultState.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.message)
                    }
                }
                is ResultState.Loading -> {}
            }
        }
    }

    fun selectVariant(variant: ProductVariantResp) {
        _uiState.update { it.copy(selectedVariant = variant) }
    }

    fun addToCart(variantId: String, quantity: Int = 1) {
        viewModelScope.launch {
            _uiState.update { it.copy(isAddingToCart = true, addToCartSuccess = false) }

            // <<< GỌI API THẬT QUA CART REPOSITORY
            when (val result = cartRepository.addToCart(variantId, quantity)) {
                is ResultState.Success -> {
                    _uiState.update { it.copy(isAddingToCart = false, addToCartSuccess = true) }
                }
                is ResultState.Error -> {
                    _uiState.update { it.copy(isAddingToCart = false, errorMessage = result.message) }
                }
                is ResultState.Loading -> {}
            }
        }
    }

    fun resetCartSuccess() {
        _uiState.update { it.copy(addToCartSuccess = false) }
    }
}