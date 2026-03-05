package com.example.prm.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.remote.dto.ProductResp
import com.example.prm.data.repository.ProductRepository
import com.example.prm.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AdminEditProductUiState(
    val isLoading: Boolean = false,
    val product: ProductResp? = null,
    val categories: List<CategoryItem> = emptyList(),
    val brands: List<BrandItem> = emptyList(),
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class AdminEditProductViewModel(
    private val productRepository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminEditProductUiState())
    val uiState: StateFlow<AdminEditProductUiState> = _uiState

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = productRepository.getProductById(productId)) {
                is ResultState.Success -> {
                    _uiState.value = _uiState.value.copy(
                        product = result.data,
                        isLoading = false,
                        errorMessage = null
                    )
                }
                is ResultState.Error -> {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = result.message,
                        isLoading = false
                    )
                }
                is ResultState.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    categories = listOf(
                        CategoryItem("1", "Badminton Rackets"),
                        CategoryItem("2", "Badminton Shuttlecocks"),
                        CategoryItem("3", "Badminton Shoes"),
                        CategoryItem("4", "Badminton Apparel"),
                        CategoryItem("5", "Badminton Bags"),
                        CategoryItem("6", "Badminton Accessories")
                    )
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to load categories"
                )
            }
        }
    }

    fun loadBrands() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    brands = listOf(
                        BrandItem("1", "Yonex"),
                        BrandItem("2", "Victor"),
                        BrandItem("3", "Lining"),
                        BrandItem("4", "Kumpoo"),
                        BrandItem("5", "Akado"),
                        BrandItem("6", "Wilson")
                    )
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to load brands"
                )
            }
        }
    }

    fun updateProduct(
        productId: String,
        productName: String,
        description: String,
        basePrice: Double,
        categoryId: String,
        brandId: String,
        slug: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, isSuccess = false)

            when (
                val result = productRepository.updateProduct(
                    id = productId,
                    categoryId = categoryId,
                    brandId = brandId,
                    productName = productName,
                    slug = slug.ifBlank { null },
                    description = description.ifBlank { null },
                    basePrice = basePrice,
                    isActive = _uiState.value.product?.isActive ?: true
                )
            ) {
                is ResultState.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true
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
}
