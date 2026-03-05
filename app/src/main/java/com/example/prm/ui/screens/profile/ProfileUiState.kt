package com.example.prm.ui.screens.profile

import com.example.prm.data.remote.dto.ProfileResponse

data class ProfileUiState(
    val isLoading: Boolean = false,
    val profile: ProfileResponse? = null,
    val error: String? = null
)