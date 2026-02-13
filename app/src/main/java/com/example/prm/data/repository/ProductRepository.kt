package com.example.prm.data.repository

import com.example.prm.data.remote.dto.*
import com.example.prm.utils.ResultState
import kotlinx.coroutines.delay

class ProductRepository {
    suspend fun getHome(): ResultState<HomeResponse> {
        return try {
            delay(300) // Simulate network delay
            ResultState.Success(MockDataProvider.getMockHomeResponse())
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getCategories(): ResultState<List<Category>> {
        return try {
            delay(300)
            ResultState.Success(MockDataProvider.getMockCategories())
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getBrands(): ResultState<List<Brand>> {
        return try {
            delay(300)
            ResultState.Success(MockDataProvider.getMockBrands())
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getProducts(
        search: String? = null,
        categoryId: Int? = null,
        brandId: Int? = null,
        page: Int = 1
    ): ResultState<ProductsResponse> {
        return try {
            delay(500)
            val allProducts = MockDataProvider.getMockProducts()
            val filtered = allProducts.filter { product ->
                (search == null || product.name.contains(search, ignoreCase = true)) &&
                (categoryId == null || product.categoryId == categoryId) &&
                (brandId == null || product.brandId == brandId)
            }
            ResultState.Success(
                ProductsResponse(
                    products = filtered,
                    total = filtered.size,
                    page = page,
                    perPage = 20
                )
            )
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getProductDetail(productId: Int): ResultState<ProductDetail> {
        return try {
            delay(400)
            ResultState.Success(MockDataProvider.getMockProductDetail(productId))
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getCart(): ResultState<CartResponse> {
        return try {
            delay(300)
            ResultState.Success(MockDataProvider.getMockCartResponse())
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getCheckoutQuote(): ResultState<CheckoutQuoteResponse> {
        return try {
            delay(300)
            ResultState.Success(MockDataProvider.getMockCheckoutQuote())
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun addToCart(productId: Int, quantity: Int): ResultState<CartItem> {
        return try {
            delay(300)
            val product = MockDataProvider.getMockProducts().find { it.id == productId }
            if (product != null) {
                ResultState.Success(
                    CartItem(
                        id = System.currentTimeMillis().toInt(),
                        productId = product.id,
                        productName = product.name,
                        variantId = null,
                        quantity = quantity,
                        price = product.price,
                        subtotal = product.price * quantity,
                        imageUrl = product.imageUrl
                    )
                )
            } else {
                ResultState.Error("Product not found")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun removeFromCart(itemId: Int): ResultState<Unit> {
        return try {
            delay(300)
            ResultState.Success(Unit)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun applyVoucher(code: String): ResultState<CartResponse> {
        return try {
            delay(300)
            val currentCart = MockDataProvider.getMockCartResponse()
            if (code == "SAVE10") {
                ResultState.Success(
                    currentCart.copy(
                        discount = currentCart.subtotal * 0.1,
                        voucherCode = code,
                        total = currentCart.subtotal * 0.9
                    )
                )
            } else {
                ResultState.Error("Invalid voucher code")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error")
        }
    }
}
