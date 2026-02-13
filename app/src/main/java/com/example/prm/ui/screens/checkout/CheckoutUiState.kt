package com.example.prm.ui.screens.checkout

import com.example.prm.data.remote.dto.CheckoutQuoteResponse

data class CheckoutUiState(
    val quote: CheckoutQuoteResponse? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isProcessing: Boolean = false,
    val orderPlaced: Boolean = false,
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val city: String = "",
    val zipCode: String = ""
)
