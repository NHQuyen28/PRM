package com.example.prm.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.repository.AuthRepository
import com.example.prm.data.session.SessionManager
import com.example.prm.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.util.Log

class RegisterViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val sessionManager: SessionManager? = null // Will be provided from the app context
) : ViewModel() {
    companion object {
        private const val TAG = "RegisterViewModel"
    }
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onFullNameChange(newFullName: String) {
        _uiState.update { it.copy(fullName = newFullName, errorMessage = null) }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail, errorMessage = null) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword, errorMessage = null) }
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = newConfirmPassword, errorMessage = null) }
    }

    fun onPhoneChange(newPhone: String) {
        _uiState.update { it.copy(phone = newPhone, errorMessage = null) }
    }

    fun register() {
        val currentFullName = _uiState.value.fullName
        val currentEmail = _uiState.value.email
        val currentPassword = _uiState.value.password
        val currentConfirmPassword = _uiState.value.confirmPassword

        // Validation
        if (currentFullName.isEmpty() || currentEmail.isEmpty() || currentPassword.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập đầy đủ thông tin!") }
            return
        }

        if (currentPassword.length < 6) {
            _uiState.update { it.copy(errorMessage = "Mật khẩu ít nhất 6 ký tự!") }
            return
        }

        if (currentPassword != currentConfirmPassword) {
            _uiState.update { it.copy(errorMessage = "Mật khẩu xác nhận không khớp!") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                Log.d(TAG, "Starting register with email: $currentEmail")
                val result = authRepository.register(
                    fullName = currentFullName,
                    email = currentEmail,
                    password = currentPassword,
                    phone = _uiState.value.phone.ifEmpty { null }
                )
                
                Log.d(TAG, "Register result: $result")
                
                when (result) {
                    is ResultState.Success -> {
                        Log.d(TAG, "Register success, saving token...")
                        // Save auth response to session
                        sessionManager?.saveAuthResponse(result.data)
                        Log.d(TAG, "Token saved, updating UI")
                        _uiState.update { 
                            it.copy(
                                isLoading = false, 
                                registerSuccess = true,
                                successMessage = "Đăng ký thành công!"
                            ) 
                        }
                    }
                    is ResultState.Error -> {
                        Log.e(TAG, "Register error: ${result.message}")
                        _uiState.update { 
                            it.copy(
                                isLoading = false, 
                                errorMessage = result.message
                            ) 
                        }
                    }
                    is ResultState.Loading -> {
                        Log.d(TAG, "Register loading...")
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Register exception", e)
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        errorMessage = e.message ?: "Unknown error"
                    ) 
                }
            }
        }
    }
}



