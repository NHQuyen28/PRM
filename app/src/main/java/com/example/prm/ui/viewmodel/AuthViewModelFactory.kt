package com.example.prm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.prm.data.repository.AuthRepository
import com.example.prm.data.session.SessionManager
import com.example.prm.ui.screens.login.LoginViewModel
import com.example.prm.ui.screens.register.RegisterViewModel

class AuthViewModelFactory(
    private val sessionManager: SessionManager,
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            LoginViewModel::class.java -> {
                LoginViewModel(authRepository, sessionManager) as T
            }
            RegisterViewModel::class.java -> {
                RegisterViewModel(authRepository, sessionManager) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
