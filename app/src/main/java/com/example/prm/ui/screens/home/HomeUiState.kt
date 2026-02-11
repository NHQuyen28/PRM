package com.example.prm.ui.screens.home

import com.example.prm.data.remote.dto.Banner
import com.example.prm.data.remote.dto.Category
import com.example.prm.data.remote.dto.Product

data class HomeUiState(
    val banners: List<Banner> = emptyList(),
    val categories: List<Category> = emptyList(),
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
