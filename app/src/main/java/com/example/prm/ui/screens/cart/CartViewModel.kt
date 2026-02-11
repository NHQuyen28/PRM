package com.example.prm.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.repository.ProductRepository
import com.example.prm.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        loadCart()
    }

    fun loadCart() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = repository.getCart()) {
                is ResultState.Success -> {
                    _uiState.update {
                        it.copy(
                            items = result.data.items,
                            subtotal = result.data.subtotal,
                            discount = result.data.discount ?: 0.0,
                            voucherCode = result.data.voucherCode,
                            total = result.data.total,
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

    fun removeItem(itemId: Int) {
        viewModelScope.launch {
            repository.removeFromCart(itemId)
            loadCart()
        }
    }

    fun applyVoucher() {
        viewModelScope.launch {
            val code = _uiState.value.voucherInput
            if (code.isNotEmpty()) {
                when (val result = repository.applyVoucher(code)) {
                    is ResultState.Success -> {
                        _uiState.update {
                            it.copy(
                                subtotal = result.data.subtotal,
                                discount = result.data.discount ?: 0.0,
                                voucherCode = result.data.voucherCode,
                                total = result.data.total,
                                voucherInput = ""
                            )
                        }
                    }
                    is ResultState.Error -> {
                        _uiState.update {
                            it.copy(errorMessage = result.message)
                        }
                    }
                    is ResultState.Loading -> {}
                }
            }
        }
    }

    fun updateVoucherInput(code: String) {
        _uiState.update { it.copy(voucherInput = code) }
    }
}
