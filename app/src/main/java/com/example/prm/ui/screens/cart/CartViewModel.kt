package com.example.prm.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.repository.CartRepository
import com.example.prm.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private val repository = CartRepository()

    private val _uiState = MutableStateFlow(CartUiState(isLoading = true))
    val uiState: StateFlow<CartUiState> = _uiState

    fun loadCart() {

        viewModelScope.launch {

            _uiState.value = CartUiState(isLoading = true)

            when (val result = repository.getCart()) {

                is ResultState.Success -> {
                    _uiState.value = CartUiState(cart = result.data)
                }

                is ResultState.Error -> {
                    _uiState.value = CartUiState(error = result.message)
                }

                else -> {}
            }
        }
    }

    fun updateQuantity(
        cartItemId: String,
        quantity: Int
    ) {

        val currentCart = _uiState.value.cart ?: return

        val updatedItems = currentCart.items.map {
            if (it.id == cartItemId) it.copy(quantity = quantity)
            else it
        }

        val updatedCart = currentCart.copy(
            items = updatedItems,
            subtotal = updatedItems.sumOf { it.unitPrice * it.quantity }
        )

        // UI UPDATE NGAY
        _uiState.value = _uiState.value.copy(cart = updatedCart)

        // CALL API BACKGROUND
        viewModelScope.launch {

            val result = repository.updateCart(cartItemId, quantity)

            if (result is ResultState.Error) {
                loadCart() // rollback
            }
        }
    }

}