package com.example.prm.ui.screens.product_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.repository.ProductRepository
import com.example.prm.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val repository = ProductRepository()
    private val productId: Int = savedStateHandle["productId"] ?: 0

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    init {
        loadProduct()
    }

    private fun loadProduct() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = repository.getProductDetail(productId)) {
                is ResultState.Success -> {
                    _uiState.update {
                        it.copy(
                            product = result.data,
                            isLoading = false
                        )
                    }
                }
                is ResultState.Error -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.message,
                            isLoading = false
                        )
                    }
                }
                is ResultState.Loading -> {}
            }
        }
    }

    fun selectVariant(variantId: Int) {
        _uiState.update { it.copy(selectedVariantId = variantId) }
    }

    fun toggleAddon(addonId: Int) {
        _uiState.update {
            val newAddons = it.selectedAddons.toMutableList()
            if (newAddons.contains(addonId)) {
                newAddons.remove(addonId)
            } else {
                newAddons.add(addonId)
            }
            it.copy(selectedAddons = newAddons)
        }
    }

    fun updateQuantity(quantity: Int) {
        if (quantity > 0) {
            _uiState.update { it.copy(quantity = quantity) }
        }
    }

    fun addToCart() {
        viewModelScope.launch {
            val state = _uiState.value
            repository.addToCart(productId, state.quantity)
        }
    }
}
