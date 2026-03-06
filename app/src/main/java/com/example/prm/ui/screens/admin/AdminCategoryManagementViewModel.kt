package com.example.prm.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.remote.api.CategoryApi
import com.example.prm.data.remote.api.CreateCategoryRequest
import com.example.prm.data.remote.api.UpdateCategoryRequest
import com.example.prm.data.remote.RetrofitClient
import com.example.prm.data.remote.dto.CategoryApi as CategoryApiDto
import com.example.prm.utils.ResultState
import com.example.prm.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AdminCategoryManagementUiState(
    val isLoading: Boolean = false,
    val categories: List<CategoryApiDto> = emptyList(),
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isSubmitting: Boolean = false,
    val editingCategory: CategoryApiDto? = null
)

class AdminCategoryManagementViewModel(
    private val categoryApi: CategoryApi = RetrofitClient.createService(CategoryApi::class.java)
) : ViewModel() {

    private val repository = ProductRepository()

    private val _uiState = MutableStateFlow(AdminCategoryManagementUiState())
    val uiState: StateFlow<AdminCategoryManagementUiState> = _uiState

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val response = categoryApi.getAllCategories()
                if (response.isSuccessful && response.body() != null) {
                    val categories = response.body()!!.data?.categories ?: emptyList()
                    _uiState.value = _uiState.value.copy(
                        categories = categories,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to load categories",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Error loading categories",
                    isLoading = false
                )
            }
        }
    }

    fun createCategory(categoryName: String, description: String? = null) {
        if (categoryName.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Category name cannot be empty")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, errorMessage = null)
            try {
                val response = categoryApi.createCategory(
                    CreateCategoryRequest(categoryName, description)
                )
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        successMessage = "Category created successfully",
                        isSubmitting = false
                    )
                    loadCategories()
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to create category",
                        isSubmitting = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Error creating category",
                    isSubmitting = false
                )
            }
        }
    }

    fun updateCategory(categoryId: String, categoryName: String, description: String? = null) {
        if (categoryName.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Category name cannot be empty")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, errorMessage = null)
            try {
                val response = categoryApi.updateCategory(
                    categoryId,
                    UpdateCategoryRequest(categoryName, description)
                )
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        successMessage = "Category updated successfully",
                        isSubmitting = false,
                        editingCategory = null
                    )
                    loadCategories()
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to update category",
                        isSubmitting = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Error updating category",
                    isSubmitting = false
                )
            }
        }
    }

    fun deleteCategory(categoryId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(errorMessage = null)
            try {
                val response = categoryApi.deleteCategory(categoryId)
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        successMessage = "Category deleted successfully"
                    )
                    loadCategories()
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to delete category"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Error deleting category"
                )
            }
        }
    }

    fun setEditingCategory(category: CategoryApiDto?) {
        _uiState.value = _uiState.value.copy(editingCategory = category)
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            successMessage = null,
            errorMessage = null
        )
    }
}
