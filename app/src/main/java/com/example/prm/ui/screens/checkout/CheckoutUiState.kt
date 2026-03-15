package com.example.prm.ui.screens.checkout

import com.example.prm.data.remote.dto.AddressResponse
import com.example.prm.data.remote.dto.CartDataDto

data class CheckoutUiState(

    val addresses: List<AddressResponse> = emptyList(),

    val selectedAddress: AddressResponse? = null,

    val cart: CartDataDto? = null,

    val isLoading: Boolean = false,

    val error: String? = null

)