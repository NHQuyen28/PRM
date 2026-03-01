package com.example.prm.ui.screens.login

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

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val sessionManager: SessionManager? = null // Will be provided from the app context
) : ViewModel() {
    companion object {
        private const val TAG = "LoginViewModel"
    }
    // Quản lý trạng thái giao diện
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Hàm cập nhật Email khi người dùng gõ
    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail, errorMessage = null) }
    }

    // Hàm cập nhật Password
    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword, errorMessage = null) }
    }

    // Hàm xử lý khi nhấn nút Sign In
    fun login() {
        val currentEmail = _uiState.value.email.trim()
        val currentPassword = _uiState.value.password.trim()

        if (currentEmail.isEmpty() || currentPassword.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập đầy đủ thông tin!") }
            return
        }

        // Validate password length
        if (currentPassword.length < 6) {
            _uiState.update { it.copy(errorMessage = "Mật khẩu ít nhất 6 ký tự!") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                Log.d(TAG, "Starting login with email: $currentEmail")
                val result = authRepository.login(currentEmail, currentPassword)
                
                Log.d(TAG, "Login result: $result")
                
                when (result) {
                    is ResultState.Success -> {
                        Log.d(TAG, "Login success, saving token...")
                        // Save auth response to session
                        sessionManager?.saveAuthResponse(result.data)
                        Log.d(TAG, "Token saved, updating UI")
                        _uiState.update { 
                            it.copy(
                                isLoading = false, 
                                loginSuccess = true,
                                successMessage = "Đăng nhập thành công!"
                            ) 
                        }
                    }
                    is ResultState.Error -> {
                        Log.e(TAG, "Login error: ${result.message}")
                        _uiState.update { 
                            it.copy(
                                isLoading = false, 
                                errorMessage = result.message
                            ) 
                        }
                    }
                    is ResultState.Loading -> {
                        Log.d(TAG, "Login loading...")
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Login exception", e)
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