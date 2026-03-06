package com.example.prm.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.remote.api.BrandApi
import com.example.prm.data.remote.api.CreateBrandRequest
import com.example.prm.data.remote.api.UpdateBrandRequest
import com.example.prm.data.remote.RetrofitClient
import com.example.prm.data.remote.dto.BrandApi as BrandApiDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AdminBrandManagementUiState(
    val isLoading: Boolean = false,
    val brands: List<BrandApiDto> = emptyList(),
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isSubmitting: Boolean = false,
    val editingBrand: BrandApiDto? = null
)

class AdminBrandManagementViewModel(
    private val brandApi: BrandApi = RetrofitClient.createService(BrandApi::class.java)
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminBrandManagementUiState())
    val uiState: StateFlow<AdminBrandManagementUiState> = _uiState

    init {
        loadBrands()
    }

    fun loadBrands() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val response = brandApi.getAllBrands()
                if (response.isSuccessful && response.body() != null) {
                    val brands = response.body()!!.data?.brands ?: emptyList()
                    _uiState.value = _uiState.value.copy(
                        brands = brands,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to load brands",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Error loading brands",
                    isLoading = false
                )
            }
        }
    }

    fun createBrand(brandName: String, description: String? = null) {
        if (brandName.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Brand name cannot be empty")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, errorMessage = null)
            try {
                val response = brandApi.createBrand(
                    CreateBrandRequest(brandName, description)
                )
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        successMessage = "Brand created successfully",
                        isSubmitting = false
                    )
                    loadBrands()
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to create brand",
                        isSubmitting = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Error creating brand",
                    isSubmitting = false
                )
            }
        }
    }

    fun updateBrand(brandId: String, brandName: String, description: String? = null) {
        if (brandName.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Brand name cannot be empty")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, errorMessage = null)
            try {
                val response = brandApi.updateBrand(
                    brandId,
                    UpdateBrandRequest(brandName, description)
                )
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        successMessage = "Brand updated successfully",
                        isSubmitting = false,
                        editingBrand = null
                    )
                    loadBrands()
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to update brand",
                        isSubmitting = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Error updating brand",
                    isSubmitting = false
                )
            }
        }
    }

    fun deleteBrand(brandId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(errorMessage = null)
            try {
                val response = brandApi.deleteBrand(brandId)
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        successMessage = "Brand deleted successfully"
                    )
                    loadBrands()
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to delete brand"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Error deleting brand"
                )
            }
        }
    }

    fun setEditingBrand(brand: BrandApiDto?) {
        _uiState.value = _uiState.value.copy(editingBrand = brand)
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            successMessage = null,
            errorMessage = null
        )
    }
}
