package com.example.prm.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.remote.dto.ProductResp
import com.example.prm.data.repository.ProductRepository
import com.example.prm.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AdminDashboardUiState(
    val isLoading: Boolean = false,
    val products: List<ProductResp> = emptyList(),
    val errorMessage: String? = null
)

class AdminDashboardViewModel(
    private val productRepository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminDashboardUiState())
    val uiState: StateFlow<AdminDashboardUiState> = _uiState

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = productRepository.getProducts(page = 1, pageSize = 100)) {
                is ResultState.Success -> {
                    _uiState.value = _uiState.value.copy(
                        products = result.data.products,
                        isLoading = false
                    )
                }
                is ResultState.Error -> {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = result.message,
                        isLoading = false
                    )
                }
                is ResultState.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            when (val result = productRepository.deleteProduct(productId)) {
                is ResultState.Success -> {
                    // Reload products after successful deletion
                    loadProducts()
                }
                is ResultState.Error -> {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = result.message
                    )
                }
                is ResultState.Loading -> {
                    // no-op
                }
            }
        }
    }
}
