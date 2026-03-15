package com.example.prm.ui.screens.product_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.repository.ProductRepository
import com.example.prm.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // GỌI API THẬT LẤY CHI TIẾT SẢN PHẨM TỪ DB
            when (val result = repository.getProductById(productId)) {
                is ResultState.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false, product = result.data)
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

    fun addToCart(productId: String, quantity: Int = 1) {
        viewModelScope.launch {
            _uiState.update { it.copy(isAddingToCart = true, addToCartSuccess = false) }
            when (val result = repository.addToCart(productId, quantity)) {
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