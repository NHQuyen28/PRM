package com.example.prm.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.remote.dto.ProductResp
import com.example.prm.data.remote.dto.ProductVariantResp
import com.example.prm.data.repository.ProductRepository
import com.example.prm.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AdminEditProductUiState(
    val isLoading: Boolean = false,
    val isVariantLoading: Boolean = false, // Loading riêng cho Variant
    val product: ProductResp? = null,
    val categories: List<CategoryItem> = emptyList(),
    val brands: List<BrandItem> = emptyList(),
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isProductUpdateSuccess: Boolean = false
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
                is ResultState.Loading -> {}
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
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
        }
    }

    fun loadBrands() {
        viewModelScope.launch {
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
        }
    }

    fun updateProduct(
        productId: String,
        productName: String,
        description: String,
        basePrice: Double,
        categoryId: String,
        brandId: String,
        slug: String,
        imageUrl: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, isProductUpdateSuccess = false)

            when (val result = productRepository.updateProduct(
                id = productId,
                categoryId = categoryId,
                brandId = brandId,
                productName = productName,
                slug = slug.ifBlank { null },
                description = description.ifBlank { null },
                basePrice = basePrice,
                imageUrl = imageUrl.ifBlank { null },
                isActive = _uiState.value.product?.isActive ?: true
            )) {
                is ResultState.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, isProductUpdateSuccess = true)
                }
                is ResultState.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = result.message)
                }
                is ResultState.Loading -> {}
            }
        }
    }

    // =====================================
    // PRODUCT VARIANT LOGIC
    // =====================================

    fun addVariant(
        productId: String, sku: String, size: String, color: String, weight: String,
        gripSize: String, stringTension: String, stockQuantity: Int, priceAdjustment: Double
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isVariantLoading = true, errorMessage = null, successMessage = null)

            when (val result = productRepository.addVariant(
                productId, sku,
                size.ifBlank { null }, color.ifBlank { null }, weight.ifBlank { null },
                gripSize.ifBlank { null }, stringTension.ifBlank { null }, stockQuantity, priceAdjustment
            )) {
                is ResultState.Success -> {
                    _uiState.value = _uiState.value.copy(isVariantLoading = false, successMessage = "Variant added successfully!")
                    loadProduct(productId) // Tải lại sản phẩm để cập nhật danh sách Variant mới
                }
                is ResultState.Error -> {
                    _uiState.value = _uiState.value.copy(isVariantLoading = false, errorMessage = result.message)
                }
                is ResultState.Loading -> {}
            }
        }
    }

    fun updateVariant(
        productId: String, variantId: String, size: String, color: String,
        stockQuantity: Int, priceAdjustment: Double, isActive: Boolean
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isVariantLoading = true, errorMessage = null, successMessage = null)

            when (val result = productRepository.updateVariant(
                variantId, size.ifBlank { null }, color.ifBlank { null }, stockQuantity, priceAdjustment, isActive
            )) {
                is ResultState.Success -> {
                    _uiState.value = _uiState.value.copy(isVariantLoading = false, successMessage = "Variant updated successfully!")
                    loadProduct(productId) // Tải lại sản phẩm
                }
                is ResultState.Error -> {
                    _uiState.value = _uiState.value.copy(isVariantLoading = false, errorMessage = result.message)
                }
                is ResultState.Loading -> {}
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(errorMessage = null, successMessage = null)
    }
}