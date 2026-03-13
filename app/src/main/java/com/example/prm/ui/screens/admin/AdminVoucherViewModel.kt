package com.example.prm.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.remote.dto.VoucherResp
import com.example.prm.data.repository.VoucherRepository
import com.example.prm.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AdminVoucherUiState(
    val isLoading: Boolean = false,
    val vouchers: List<VoucherResp> = emptyList(),
    val errorMessage: String? = null
)

class AdminVoucherViewModel(
    private val voucherRepository: VoucherRepository = VoucherRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminVoucherUiState())
    val uiState: StateFlow<AdminVoucherUiState> = _uiState

    init {
        loadVouchers()
    }

    fun loadVouchers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = voucherRepository.getAllVouchers(page = 1, pageSize = 100)) {
                is ResultState.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        vouchers = result.data.vouchers
                    )
                }
                is ResultState.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is ResultState.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun deleteVoucher(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = voucherRepository.deleteVoucher(id)) {
                is ResultState.Success -> {
                    // Tải lại danh sách sau khi xóa thành công
                    loadVouchers()
                }
                is ResultState.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is ResultState.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }
}