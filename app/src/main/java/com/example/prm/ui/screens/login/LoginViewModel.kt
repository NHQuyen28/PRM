package com.example.prm.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.repository.AuthRepository
import com.example.prm.data.session.SessionManager
import com.example.prm.ui.screens.login.LoginUiState
import com.example.prm.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {

            _uiState.value = LoginUiState(isLoading = true)

            val result = repository.login(email, password)

            when (result) {
                is ResultState.Success -> {
                    sessionManager.saveToken(result.data.accessToken)
                    _uiState.value = LoginUiState(isSuccess = true)
                }

                is ResultState.Error -> {
                    _uiState.value = LoginUiState(
                        errorMessage = result.message
                    )
                }

                is ResultState.Loading -> Unit
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState()
    }
}
