package com.example.prm.ui.screens.profile

import com.example.prm.data.remote.dto.AddressResponse
import com.example.prm.data.remote.dto.OrderResponse
import com.example.prm.data.remote.dto.ProfileResponse

data class ProfileUiState(
    val isLoading: Boolean = false,
    val profile: ProfileResponse? = null,
    val addresses: List<AddressResponse> = emptyList(),
    val orders: List<OrderResponse> = emptyList(),
    val error: String? = null,
    val isAuthError: Boolean = false
)