package com.example.prm.ui.screens.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.repository.ProductRepository
import com.example.prm.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.util.Log

class ProductListViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    companion object {
        private const val TAG = "ProductListViewModel"
    }

    init {
        loadProducts()
    }

    fun loadProducts(page: Int = 1) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            Log.d(TAG, "Loading products page: $page")
            
            when (val result = repository.getProducts(page = page, pageSize = 20)) {
                is ResultState.Success -> {
                    val products = result.data.products.mapIndexed { index, resp ->
                        com.example.prm.data.remote.dto.Product(
                            id = index + 1, // Use index as ID for now
                            name = resp.productName,
                            description = resp.description,
                            price = resp.basePrice,
                            originalPrice = null,
                            imageUrl = resp.images?.firstOrNull { it.isPrimary }?.imageUrl ?: "",
                            rating = null,
                            reviewCount = null,
                            brandId = 1,
                            categoryId = 1
                        )
                    }
                    
                    Log.d(TAG, "Products loaded: ${products.size} items")
                    _uiState.update {
                        it.copy(
                            products = products,
                            isLoading = false,
                            currentPage = page,
                            totalPages = result.data.pagination.totalPages
                        )
                    }
                }
                is ResultState.Error -> {
                    Log.e(TAG, "Error loading products: ${result.message}")
                    _uiState.update {
                        it.copy(
                            errorMessage = result.message,
                            isLoading = false
                        )
                    }
                }
                is ResultState.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun onSearchChange(query: String) {
        _uiState.update { it.copy(searchQuery = query, currentPage = 1) }
        loadProducts()
    }

    fun onCategorySelect(categoryId: Int?) {
        _uiState.update { it.copy(selectedCategoryId = categoryId, currentPage = 1) }
        loadProducts()
    }

    fun onSortChange(sortBy: String) {
        _uiState.update { it.copy(sortBy = sortBy, currentPage = 1) }
        loadProducts()
    }
}
