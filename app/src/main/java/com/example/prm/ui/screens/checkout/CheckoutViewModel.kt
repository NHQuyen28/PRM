package com.example.prm.ui.screens.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.repository.ProductRepository
import com.example.prm.utils.ResultState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckoutViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    init {
        loadCheckoutQuote()
    }

    private fun loadCheckoutQuote() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = repository.getCheckoutQuote()) {
                is ResultState.Success -> {
                    _uiState.update {
                        it.copy(
                            quote = result.data,
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

    fun updateFullName(name: String) {
        _uiState.update { it.copy(fullName = name) }
    }

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun updatePhone(phone: String) {
        _uiState.update { it.copy(phone = phone) }
    }

    fun updateAddress(address: String) {
        _uiState.update { it.copy(address = address) }
    }

    fun updateCity(city: String) {
        _uiState.update { it.copy(city = city) }
    }

    fun updateZipCode(zipCode: String) {
        _uiState.update { it.copy(zipCode = zipCode) }
    }

    fun placeOrder() {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }
            delay(2000) // Simulate API call
            _uiState.update { it.copy(isProcessing = false, orderPlaced = true) }
        }
    }
}
