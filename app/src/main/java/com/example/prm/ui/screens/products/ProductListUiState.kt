package com.example.prm.ui.screens.products

import com.example.prm.data.remote.dto.Category
import com.example.prm.data.remote.dto.Product

data class ProductListUiState(
    val products: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val selectedCategoryId: Int? = null,
    val sortBy: String = "latest",
    val currentPage: Int = 1,
    val totalPages: Int = 1
)
