package com.example.prm.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.remote.RetrofitClient
import com.example.prm.data.remote.api.BrandApi
import com.example.prm.data.remote.api.CategoryApi
import com.example.prm.data.repository.ProductRepository
import com.example.prm.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AdminAddProductUiState(
    val isLoading: Boolean = false,
    val categories: List<CategoryItem> = emptyList(),
    val brands: List<BrandItem> = emptyList(),
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class AdminAddProductViewModel(
    private val productRepository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val categoryApi = RetrofitClient.createService(CategoryApi::class.java)
    private val brandApi = RetrofitClient.createService(BrandApi::class.java)

    private val _uiState = MutableStateFlow(AdminAddProductUiState())
    val uiState: StateFlow<AdminAddProductUiState> = _uiState

    fun loadCategories() {
        viewModelScope.launch {
            try {
                val response = categoryApi.getAllCategories()
                if (response.isSuccessful && response.body()?.data != null) {
                    val categories = response.body()!!.data!!.categories.map { cat ->
                        CategoryItem(cat.id, cat.name)
                    }
                    _uiState.value = _uiState.value.copy(categories = categories)
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to load categories"
                    )
                }
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
                val response = brandApi.getAllBrands()
                if (response.isSuccessful && response.body()?.data != null) {
                    val brands = response.body()!!.data!!.brands.map { brand ->
                        BrandItem(brand.id, brand.name)
                    }
                    _uiState.value = _uiState.value.copy(brands = brands)
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to load brands"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to load brands"
                )
            }
        }
    }

    // ĐÃ THÊM imageUrl VÀO ĐÂY
    fun createProduct(
        productName: String,
        description: String,
        basePrice: Double,
        categoryId: String,
        brandId: String,
        slug: String,
        imageUrl: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, isSuccess = false)

            when (
                // LƯU Ý: Chỗ này sẽ báo lỗi đỏ (Unresolved reference: imageUrl)
                // vì chúng ta chưa cập nhật ProductRepository. Đừng lo, ta sẽ sửa nó ở Bước 2.
                val result = productRepository.createProduct(
                    categoryId = categoryId,
                    brandId = brandId,
                    productName = productName,
                    slug = slug.ifBlank { null },
                    description = description.ifBlank { null },
                    basePrice = basePrice,
                    imageUrl = imageUrl.ifBlank { null }
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