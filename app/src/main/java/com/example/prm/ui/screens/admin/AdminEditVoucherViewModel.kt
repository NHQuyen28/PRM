package com.example.prm.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.remote.dto.UpdateVoucherReq
import com.example.prm.data.remote.dto.VoucherResp
import com.example.prm.data.repository.VoucherRepository
import com.example.prm.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AdminEditVoucherUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val voucher: VoucherResp? = null,
    val errorMessage: String? = null
)

class AdminEditVoucherViewModel(
    private val voucherRepository: VoucherRepository = VoucherRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminEditVoucherUiState())
    val uiState: StateFlow<AdminEditVoucherUiState> = _uiState

    fun loadVoucher(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = voucherRepository.getVoucherById(id)) {
                is ResultState.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, voucher = result.data)
                }
                is ResultState.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = result.message)
                }
                is ResultState.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun updateVoucher(
        id: String, code: String, name: String, description: String,
        discountAmount: Double, discountPercentage: Double,
        minOrder: Double, maxDiscount: Double, usageLimit: Int,
        startDate: String, endDate: String, isActive: Boolean
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, isSuccess = false)

            // Xử lý ngày tháng chuẩn UTC cho Backend
            val formattedStart = if (startDate.length == 10) "${startDate}T00:00:00.000Z" else startDate
            val formattedEnd = if (endDate.length == 10) "${endDate}T23:59:59.999Z" else endDate

            val request = UpdateVoucherReq(
                id = id,
                code = code,
                name = name,
                description = description.ifBlank { null },
                discountAmount = discountAmount,
                discountPercentage = discountPercentage,
                minimumOrderAmount = minOrder,
                maximumDiscountAmount = maxDiscount,
                usageLimit = usageLimit,
                startDate = formattedStart,
                endDate = formattedEnd,
                isActive = isActive
            )

            when (val result = voucherRepository.updateVoucher(request)) {
                is ResultState.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
                }
                is ResultState.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = result.message)
                }
                is ResultState.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }
}