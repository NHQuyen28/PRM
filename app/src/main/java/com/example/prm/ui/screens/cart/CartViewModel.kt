package com.example.prm.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.remote.dto.VoucherResp
import com.example.prm.data.repository.CartRepository
import com.example.prm.data.repository.VoucherRepository
import com.example.prm.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private val repository = CartRepository()

    private val voucherRepository = VoucherRepository()

    private val _vouchers = MutableStateFlow<List<VoucherResp>>(emptyList())
    val vouchers: StateFlow<List<VoucherResp>> = _vouchers

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

    fun updateQuantity(cartItemId: String, quantity: Int) {
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
            // ĐÃ SỬA 1: Đổi thành updateCartItem thay vì updateCart
            // ĐÃ SỬA 2: Dùng cấu trúc when thay cho if để không bị lỗi Generic Type
            when (repository.updateCartItem(cartItemId, quantity)) {
                is ResultState.Error -> {
                    loadCart() // rollback lại giỏ hàng cũ nếu API báo lỗi
                }
                else -> {}
            }
        }
    }

    fun removeFromCart(cartItemId: String) {
        val currentCart = _uiState.value.cart ?: return

        val updatedItems = currentCart.items.filter { it.id != cartItemId }
        val updatedCart = currentCart.copy(
            items = updatedItems,
            subtotal = updatedItems.sumOf { it.unitPrice * it.quantity }
        )

        // UI UPDATE NGAY
        _uiState.value = _uiState.value.copy(cart = updatedCart)

        // CALL API BACKGROUND
        viewModelScope.launch {
            // ĐÃ SỬA 2: Dùng cấu trúc when thay cho if
            when (repository.removeFromCart(cartItemId)) {
                is ResultState.Error -> {
                    loadCart() // rollback lại giỏ hàng cũ nếu API báo lỗi
                }
                else -> {}
            }
        }
    }

    fun loadVouchers() {
        viewModelScope.launch {
            when (val result = voucherRepository.getAllVouchers()) {
                is ResultState.Success -> {
                    _vouchers.value = result.data.vouchers
                }
                else -> {}
            }
        }
    }

    fun applyVoucher(voucher: VoucherResp) {
        val cart = _uiState.value.cart ?: return

        val subtotal = cart.subtotal

        val discount = when {
            voucher.discountAmount > 0 -> voucher.discountAmount

            voucher.discountPercentage > 0 -> {
                val percent = subtotal * voucher.discountPercentage / 100
                if (voucher.maximumDiscountAmount > 0)
                    minOf(percent, voucher.maximumDiscountAmount)
                else percent
            }

            else -> 0.0
        }

        _uiState.value = _uiState.value.copy(
            selectedVoucher = voucher,
            discountAmount = discount
        )
    }
}