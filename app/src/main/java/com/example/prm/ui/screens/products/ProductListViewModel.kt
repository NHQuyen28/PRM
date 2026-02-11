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

class ProductListViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
        loadProducts()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            when (val result = repository.getCategories()) {
                is ResultState.Success -> {
                    _uiState.update { it.copy(categories = result.data) }
                }
                is ResultState.Error -> {
                    _uiState.update { it.copy(errorMessage = result.message) }
                }
                is ResultState.Loading -> {}
            }
        }
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val state = _uiState.value
            when (val result = repository.getProducts(
                search = if (state.searchQuery.isNotEmpty()) state.searchQuery else null,
                categoryId = state.selectedCategoryId,
                page = state.currentPage
            )) {
                is ResultState.Success -> {
                    _uiState.update {
                        it.copy(
                            products = result.data.products,
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
