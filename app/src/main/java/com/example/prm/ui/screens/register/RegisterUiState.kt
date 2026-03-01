package com.example.prm.ui.screens.register

data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val phone: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val registerSuccess: Boolean = false,
    val successMessage: String? = null
)



