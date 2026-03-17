package com.example.prm.ui.screens.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.remote.dto.AddressResponse
import com.example.prm.data.remote.dto.CreateOrderRequest
import com.example.prm.data.repository.AddressRepository
import com.example.prm.data.repository.CartRepository
import com.example.prm.data.repository.OrderRepository
import com.example.prm.data.repository.PaymentRepository
import com.example.prm.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CheckoutViewModel : ViewModel() {

    private val addressRepository = AddressRepository()

    private val orderRepository = OrderRepository()

    private val cartRepository = CartRepository()

    private val paymentRepository = PaymentRepository()

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState


    fun loadAddresses() {

        viewModelScope.launch {

            when (val result = addressRepository.getAddresses()) {

                is ResultState.Success -> {

                    val defaultAddress = result.data.find { it.isDefault }

                    _uiState.value = _uiState.value.copy(
                        addresses = result.data,
                        selectedAddress = defaultAddress
                    )
                }

                is ResultState.Error -> {

                    _uiState.value = _uiState.value.copy(
                        error = result.message
                    )

                }

                else -> {}
            }

        }
    }

    fun selectAddress(address: com.example.prm.data.remote.dto.AddressResponse) {

        _uiState.value = _uiState.value.copy(
            selectedAddress = address
        )

    }
    fun placeOrder() {

        viewModelScope.launch {

            val address = _uiState.value.selectedAddress ?: return@launch
            val cart = _uiState.value.cart ?: return@launch

            // 🔥 THÊM ĐOẠN NÀY
            val shipping = 30000.0
            val discount = _uiState.value.discountAmount
            val totalAmount = cart.subtotal + shipping - discount

            val request = CreateOrderRequest(

                addressId = address.id,

                recipientName = address.recipientName,

                recipientPhone = address.phone,

                shippingAddress = address.fullAddress,

                voucherId = _uiState.value.selectedVoucherId,

                // 🔥 QUAN TRỌNG NHẤT
                totalAmount = totalAmount,

                paymentMethod = 0,

                notes = null
            )

            when (val orderResult = orderRepository.createOrder(request)) {

                is ResultState.Success -> {

                    val order = orderResult.data

                    createMomoPayment(
                        orderId = order.id,

                        // 🔥 ĐỪNG DÙNG order.totalAmount nữa
                        amount = totalAmount
                    )

                }

                is ResultState.Error -> {

                    _uiState.value = _uiState.value.copy(
                        error = orderResult.message
                    )

                }

                else -> {}

            }

        }

    }

    fun loadCart() {

        viewModelScope.launch {

            when (val result = cartRepository.getCart()) {

                is ResultState.Success -> {

                    _uiState.value = _uiState.value.copy(
                        cart = result.data
                    )

                }

                is ResultState.Error -> {

                    _uiState.value = _uiState.value.copy(
                        error = result.message
                    )

                }

                else -> {}

            }

        }

    }

    private suspend fun createMomoPayment(
        orderId: String,
        amount: Double
    ) {

        when (val result = paymentRepository.createMomoPayment(orderId, amount)) {

            is ResultState.Success -> {

                _uiState.value = _uiState.value.copy(
                    paymentUrl = result.data
                )

            }

            is ResultState.Error -> {

                _uiState.value = _uiState.value.copy(
                    error = result.message
                )

            }

            else -> {}

        }

    }

    fun setVoucher(discount: Double, voucherId: String?) {
        _uiState.value = _uiState.value.copy(
            discountAmount = discount,
            selectedVoucherId = voucherId
        )
    }

    fun clearPaymentUrl() {
        _uiState.value = _uiState.value.copy(paymentUrl = null)
    }

}