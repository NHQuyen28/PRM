package com.example.prm.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.remote.dto.AddressResponse
import com.example.prm.data.remote.dto.CreateAddressRequest
import com.example.prm.data.remote.dto.OrderResponse
import com.example.prm.data.remote.dto.UpdateProfileRequest
import com.example.prm.data.repository.AccountRepository
import com.example.prm.data.repository.AddressRepository
import com.example.prm.data.repository.OrderRepository
import com.example.prm.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val repository = AccountRepository()

    private val addressRepository = AddressRepository()

    private val orderRepository = OrderRepository()
    val orders: List<OrderResponse> = emptyList()
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

    fun createAddress(
        recipientName: String,
        phone: String,
        province: String,
        district: String,
        ward: String,
        detailAddress: String,
        isDefault: Boolean
    ) {

        viewModelScope.launch {

            val request = CreateAddressRequest(
                id = null,
                recipientName = recipientName,
                phone = phone,
                province = province,
                district = district,
                ward = ward,
                detailAddress = detailAddress,
                isDefault = isDefault
            )

            addressRepository.createAddress(request)

            loadAddresses() // reload list
        }
    }

    fun loadAddresses() {

        viewModelScope.launch {

            when (val result = addressRepository.getAddresses()) {

                is ResultState.Success -> {
                    _uiState.value = _uiState.value.copy(
                        addresses = result.data
                    )
                }

                is ResultState.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.message
                    )
                }

                else -> {}
            }

        }

    }

    fun setDefaultAddress(id: String) {

        viewModelScope.launch {

            when (val result = addressRepository.setDefaultAddress(id)) {

                is ResultState.Success -> {
                    loadAddresses()
                }

                is ResultState.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.message
                    )
                }

                else -> {}
            }
        }
    }

    fun deleteAddress(id: String) {

        viewModelScope.launch {

            when (val result = addressRepository.deleteAddress(id)) {

                is ResultState.Success -> {
                    loadAddresses()
                }

                is ResultState.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.message
                    )
                }

                else -> {}
            }
        }
    }

    fun updateAddress(
        id: String,
        recipientName: String,
        phone: String,
        province: String,
        district: String,
        ward: String,
        detailAddress: String,
        isDefault: Boolean
    ) {

        viewModelScope.launch {

            val request = CreateAddressRequest(
                id = id,
                recipientName = recipientName,
                phone = phone,
                province = province,
                district = district,
                ward = ward,
                detailAddress = detailAddress,
                isDefault = isDefault
            )

            when (addressRepository.updateAddress(request)) {

                is ResultState.Success -> {
                    loadAddresses()
                }

                is ResultState.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = "Update failed"
                    )
                }

                else -> {}
            }

        }
    }

    fun loadOrders() {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(isLoading = true)

            when (val result = orderRepository.getOrders()) {

                is ResultState.Success -> {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        orders = result.data
                    )

                }

                is ResultState.Error -> {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )

                }

                else -> {}
            }

        }

    }


}