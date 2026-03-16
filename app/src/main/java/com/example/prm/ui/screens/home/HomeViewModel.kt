package com.example.prm.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm.data.repository.ProductRepository
import com.example.prm.data.session.SessionManager
import com.example.prm.utils.ResultState
import com.example.prm.data.remote.dto.Banner
import com.example.prm.data.remote.dto.Category
import com.example.prm.data.remote.dto.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val sessionManager: SessionManager? = null
) : ViewModel() {
    private val repository = ProductRepository()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut.asStateFlow()

    fun loadHome() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // 1. TẠO MOCK DATA CHO BANNER (Vì BE chưa có bảng Banner)
            val mockBanners = listOf(
                Banner(id = "1", imageUrl = "https://img.freepik.com/free-vector/badminton-tournament-banner-template_23-2149308197.jpg", title = "Mùa Giải Mới", discountPercent = 20),
                Banner(id = "2", imageUrl = "https://img.freepik.com/free-vector/badminton-concept-with-racket-shuttlecock_23-2148174577.jpg", title = "Vợt Chuyên Nghiệp", discountPercent = 15)
            )

            // 2. TẠO MOCK DATA CHO CATEGORY (Để trang chủ có icon đẹp)
            val mockCategories = listOf(
                Category(id = "1", name = "Vợt Cầu Lông", iconUrl = "https://cdn-icons-png.flaticon.com/512/5146/5146001.png"),
                Category(id = "2", name = "Giày Thể Thao", iconUrl = "https://cdn-icons-png.flaticon.com/512/3256/3256116.png"),
                Category(id = "3", name = "Quần Áo", iconUrl = "https://cdn-icons-png.flaticon.com/512/2806/2806085.png"),
                Category(id = "4", name = "Phụ Kiện", iconUrl = "https://cdn-icons-png.flaticon.com/512/1041/1041248.png")
            )

            // 3. GỌI API LẤY SẢN PHẨM THẬT TỪ DATABASE (.NET BACKEND)
            // Lấy 6 sản phẩm mới nhất để show ở trang chủ
            when (val result = repository.getProducts(page = 1, pageSize = 6)) {
                is ResultState.Success -> {
                    // Map dữ liệu từ ProductResp (Backend trả về) sang Product (UI cần)
                    val realProducts = result.data.products.map { resp ->
                        Product(
                            id = resp.id,
                            name = resp.productName,
                            description = resp.description,
                            price = resp.basePrice,
                            originalPrice = resp.basePrice * 1.25,
                            imageUrl = resp.images?.firstOrNull { it.isPrimary }?.imageUrl
                                ?: resp.images?.firstOrNull()?.imageUrl
                                ?: "https://m.media-amazon.com/images/I/61r5T4X-f6L._AC_SX679_.jpg",
                            // Gán cứng số giả lập cho đẹp giao diện vì API List chưa trả về rating
                            rating = 4.8,
                            reviewCount = 128,
                            brandId = resp.brandId,
                            categoryId = resp.categoryId
                        )
                    }

                    _uiState.update {
                        it.copy(
                            banners = mockBanners,
                            categories = mockCategories,
                            products = realProducts, // <<< ĐƯA SẢN PHẨM THẬT VÀO ĐÂY
                            isLoading = false
                        )
                    }
                }
                is ResultState.Error -> {
                    _uiState.update {
                        it.copy(
                            banners = mockBanners,
                            categories = mockCategories,
                            errorMessage = result.message,
                            isLoading = false
                        )
                    }
                }
                is ResultState.Loading -> {}
            }
        }
    }

//    fun addToCart(productId: String) {
//        viewModelScope.launch {
//            repository.addToCart(productId, 1)
//        }
//    }

    fun logout() {
        sessionManager?.logout()
        _isLoggedOut.value = true
    }
}