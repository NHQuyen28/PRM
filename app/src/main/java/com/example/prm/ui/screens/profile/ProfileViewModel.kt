package com.example.prm.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.remote.dto.UpdateProfileRequest
import com.example.prm.data.repository.AccountRepository
import com.example.prm.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val repository = AccountRepository()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun loadProfile() {

        viewModelScope.launch {

            _uiState.value = ProfileUiState(isLoading = true)

            when (val result = repository.getProfile()) {

                is ResultState.Success -> {
                    _uiState.value = ProfileUiState(
                        profile = result.data
                    )
                }

                is ResultState.Error -> {
                    _uiState.value = ProfileUiState(
                        error = result.message
                    )
                }

                else -> {}
            }
        }
    }

    fun updateProfile(
        fullName: String,
        phone: String,
        address: String,
        avatarUrl: String
    ) {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(isLoading = true)

            val request = UpdateProfileRequest(
                fullName = fullName,
                phone = phone,
                address = address,
                avatarUrl = avatarUrl
            )

            when (val result = repository.updateProfile(request)) {

                is ResultState.Success -> {
                    _uiState.value = ProfileUiState(
                        profile = result.data
                    )
                }

                is ResultState.Error -> {
                    _uiState.value = ProfileUiState(
                        error = result.message
                    )
                }

                else -> {}
            }
        }
    }

}