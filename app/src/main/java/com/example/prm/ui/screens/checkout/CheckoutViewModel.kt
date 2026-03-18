package com.example.prm.ui.screens.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.remote.dto.AddressResponse
import com.example.prm.data.remote.dto.CreateOrderRequest
import com.example.prm.data.repository.AddressRepository
import com.example.prm.data.repository.CartRepository
import com.example.prm.data.repository.OrderRepository
import com.example.prm.data.repository.VnPayRepository
import com.example.prm.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CheckoutViewModel : ViewModel() {

    private val addressRepository = AddressRepository()

    private val orderRepository = OrderRepository()

    private val cartRepository = CartRepository()


    private val vnPayRepository = VnPayRepository()


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

            val shipping = 30000.0
            val discount = _uiState.value.discountAmount
            val totalAmount = cart.subtotal + shipping - discount

            val request = CreateOrderRequest(
                addressId = address.id,
                recipientName = address.recipientName,
                recipientPhone = address.phone,
                shippingAddress = address.fullAddress,
                voucherId = _uiState.value.selectedVoucherId,
                totalAmount = totalAmount,
                paymentMethod = 1,
                notes = null
            )

            when (val orderResult = orderRepository.createOrder(request)) {

                is ResultState.Success -> {

                    val order = orderResult.data

                    createVnPayPayment(
                        orderId = order.id,
                        amount = totalAmount,
                        fullName = address.recipientName
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

    private fun createVnPayPayment(
        orderId: String,
        amount: Double,
        fullName: String
    ) {
        viewModelScope.launch {

            when (val result = vnPayRepository.createPayment(orderId, amount, fullName)) {

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