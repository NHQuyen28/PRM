package com.example.prm.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.repository.ProductRepository
import com.example.prm.data.session.SessionManager
import com.example.prm.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val sessionManager: SessionManager? = null
) : ViewModel() {
    private val repository = ProductRepository()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut.asStateFlow()

    fun loadHome() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = repository.getHome()) {
                is ResultState.Success -> {
                    _uiState.update {
                        it.copy(
                            banners = result.data.banners ?: emptyList(),
                            categories = result.data.featuredCategories ?: emptyList(),
                            products = result.data.featuredProducts ?: emptyList(),
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

    fun addToCart(productId: Int) {
        viewModelScope.launch {
            repository.addToCart(productId, 1)
        }
    }

    fun logout() {
        sessionManager?.logout()
        _isLoggedOut.value = true
    }
}

