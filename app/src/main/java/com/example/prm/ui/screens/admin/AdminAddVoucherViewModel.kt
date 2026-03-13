package com.example.prm.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.remote.dto.CreateVoucherReq
import com.example.prm.data.repository.VoucherRepository
import com.example.prm.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AdminAddVoucherUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class AdminAddVoucherViewModel(
    private val voucherRepository: VoucherRepository = VoucherRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminAddVoucherUiState())
    val uiState: StateFlow<AdminAddVoucherUiState> = _uiState

    fun createVoucher(
        code: String, name: String, description: String,
        discountAmount: Double, discountPercentage: Double,
        minOrder: Double, maxDiscount: Double, usageLimit: Int,
        startDate: String, endDate: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, isSuccess = false)

            // Thêm .000Z vào cuối để khớp hoàn toàn với chuẩn ISO 8601 UTC mà .NET mong đợi
            val formattedStart = if (startDate.length == 10) "${startDate}T00:00:00.000Z" else startDate
            val formattedEnd = if (endDate.length == 10) "${endDate}T23:59:59.999Z" else endDate

            val request = CreateVoucherReq(
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
                isActive = true
            )

            when (val result = voucherRepository.createVoucher(request)) {
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