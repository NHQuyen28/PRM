package com.example.prm.data.repository

import com.example.prm.data.remote.api.CreateProductRequest
import com.example.prm.data.remote.api.ProductApi
import com.example.prm.data.remote.api.UpdateProductRequest
import com.example.prm.data.remote.dto.*
import com.example.prm.data.remote.RetrofitClient
import com.example.prm.utils.ResultState
import android.util.Log
import com.google.gson.Gson

class ProductRepository {
    private val productApi = RetrofitClient.createService(ProductApi::class.java)
    private val gson = Gson()
    
    companion object {
        private const val TAG = "ProductRepository"
    }
    
    suspend fun getProducts(
        page: Int = 1,
        pageSize: Int = 20
    ): ResultState<ProductListResponse> {
        return try {
            Log.d(TAG, "Fetching products: page=$page, pageSize=$pageSize")
            val response = productApi.getProducts(page = page, pageSize = pageSize)

            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    Log.d(TAG, "Products fetched successfully: ${apiResponse.data.products.size} items")
                    ResultState.Success(apiResponse.data)
                } else {
                    val errorMsg = apiResponse.message ?: "Failed to fetch products"
                    Log.e(TAG, "API returned error: $errorMsg")
                    ResultState.Error(errorMsg)
                }
            } else {
                var errorMessage = "HTTP ${response.code()}: Failed to fetch products"
                try {
                    val errorBody = response.errorBody()?.string()
                    if (errorBody != null) {
                        val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                        if (errorResponse.message != null) {
                            errorMessage = errorResponse.message
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing error response", e)
                }
                Log.e(TAG, "Failed to fetch products: $errorMessage")
                ResultState.Error(errorMessage)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception fetching products", e)
            ResultState.Error(e.message ?: "Connection error")
        }
    }

    suspend fun getProductById(productId: String): ResultState<ProductResp> {
        return try {
            Log.d(TAG, "Fetching product by id: $productId")
            val response = productApi.getProductById(productId)

            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    ResultState.Success(apiResponse.data)
                } else {
                    val errorMsg = apiResponse.message ?: "Failed to load product"
                    Log.e(TAG, "API returned error: $errorMsg")
                    ResultState.Error(errorMsg)
                }
            } else {
                var errorMessage = "HTTP ${response.code()}: Failed to load product"
                try {
                    val errorBody = response.errorBody()?.string()
                    if (errorBody != null) {
                        val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                        if (errorResponse.message != null) {
                            errorMessage = errorResponse.message
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing error response", e)
                }
                Log.e(TAG, "Failed to load product: $errorMessage")
                ResultState.Error(errorMessage)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception fetching product", e)
            ResultState.Error(e.message ?: "Connection error")
        }
    }

    suspend fun createProduct(
        categoryId: String,
        brandId: String,
        productName: String,
        slug: String?,
        description: String?,
        basePrice: Double,
        isActive: Boolean = true
    ): ResultState<ProductResp> {
        return try {
            Log.d(TAG, "Creating product: name=$productName")
            val request = CreateProductRequest(
                categoryId = categoryId,
                brandId = brandId,
                productName = productName,
                slug = slug,
                description = description,
                basePrice = basePrice,
                isActive = isActive,
                variants = null,
                imageUrls = null
            )

            val response = productApi.createProduct(request)

            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    ResultState.Success(apiResponse.data)
                } else {
                    val errorMsg = apiResponse.message ?: "Failed to create product"
                    Log.e(TAG, "API returned error: $errorMsg")
                    ResultState.Error(errorMsg)
                }
            } else {
                var errorMessage = "HTTP ${response.code()}: Failed to create product"
                try {
                    val errorBody = response.errorBody()?.string()
                    if (errorBody != null) {
                        val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                        if (errorResponse.message != null) {
                            errorMessage = errorResponse.message
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing error response", e)
                }
                Log.e(TAG, "Failed to create product: $errorMessage")
                ResultState.Error(errorMessage)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception creating product", e)
            ResultState.Error(e.message ?: "Connection error")
        }
    }

    suspend fun updateProduct(
        id: String,
        categoryId: String,
        brandId: String,
        productName: String,
        slug: String?,
        description: String?,
        basePrice: Double,
        isActive: Boolean
    ): ResultState<ProductResp> {
        return try {
            Log.d(TAG, "Updating product: id=$id, name=$productName")
            val request = UpdateProductRequest(
                id = id,
                categoryId = categoryId,
                brandId = brandId,
                productName = productName,
                slug = slug,
                description = description,
                basePrice = basePrice,
                isActive = isActive
            )

            val response = productApi.updateProduct(request)

            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    ResultState.Success(apiResponse.data)
                } else {
                    val errorMsg = apiResponse.message ?: "Failed to update product"
                    Log.e(TAG, "API returned error: $errorMsg")
                    ResultState.Error(errorMsg)
                }
            } else {
                var errorMessage = "HTTP ${response.code()}: Failed to update product"
                try {
                    val errorBody = response.errorBody()?.string()
                    if (errorBody != null) {
                        val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                        if (errorResponse.message != null) {
                            errorMessage = errorResponse.message
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing error response", e)
                }
                Log.e(TAG, "Failed to update product: $errorMessage")
                ResultState.Error(errorMessage)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception updating product", e)
            ResultState.Error(e.message ?: "Connection error")
        }
    }

    suspend fun deleteProduct(productId: String): ResultState<Unit> {
        return try {
            Log.d(TAG, "Deleting product: id=$productId")
            val response = productApi.deleteProduct(productId)

            if (response.isSuccessful) {
                ResultState.Success(Unit)
            } else {
                var errorMessage = "HTTP ${response.code()}: Failed to delete product"
                try {
                    val errorBody = response.errorBody()?.string()
                    if (errorBody != null) {
                        val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                        if (errorResponse.message != null) {
                            errorMessage = errorResponse.message
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing error response", e)
                }
                Log.e(TAG, "Failed to delete product: $errorMessage")
                ResultState.Error(errorMessage)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception deleting product", e)
            ResultState.Error(e.message ?: "Connection error")
        }
    }

    suspend fun getCart(): ResultState<CartResponse> {
        return try {
            Log.d(TAG, "Fetching cart")
            // Mock data for now - backend cart API can be integrated later
            ResultState.Success(
                CartResponse(
                    items = emptyList(),
                    subtotal = 0.0,
                    discount = null,
                    voucherCode = null,
                    total = 0.0
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching cart", e)
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun removeFromCart(itemId: String): ResultState<Unit> {
        return try {
            Log.d(TAG, "Removing item from cart: $itemId")
            ResultState.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error removing from cart", e)
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun applyVoucher(code: String): ResultState<CartResponse> {
        return try {
            Log.d(TAG, "Applying voucher: $code")
            ResultState.Error("Invalid voucher code")
        } catch (e: Exception) {
            Log.e(TAG, "Error applying voucher", e)
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getHome(): ResultState<HomeResponse> {
        return try {
            Log.d(TAG, "Fetching home data")
            // Mock data for now - backend home API can be integrated later
            ResultState.Success(
                HomeResponse(
                    banners = emptyList(),
                    featuredCategories = emptyList(),
                    featuredProducts = emptyList()
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching home", e)
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getProductDetail(productId: String): ResultState<ProductDetail> {
        return try {
            Log.d(TAG, "Fetching product detail: $productId")
            // Use mock data for now
            ResultState.Success(MockDataProvider.getMockProductDetail(productId))
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching product detail", e)
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun addToCart(productId: String, quantity: Int): ResultState<CartItem> {
        return try {
            Log.d(TAG, "Adding to cart: productId=$productId, quantity=$quantity")
            ResultState.Success(
                CartItem(
                    id = "cart-item-${System.currentTimeMillis()}",
                    productId = productId,
                    productName = "Product $productId",
                    variantId = null,
                    quantity = quantity,
                    price = 100.0,
                    subtotal = 100.0 * quantity,
                    imageUrl = ""
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error adding to cart", e)
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getCheckoutQuote(): ResultState<CheckoutQuoteResponse> {
        return try {
            Log.d(TAG, "Fetching checkout quote")
            ResultState.Success(
                CheckoutQuoteResponse(
                    subtotal = 0.0,
                    shippingFee = 0.0,
                    discount = 0.0,
                    tax = 0.0,
                    total = 0.0
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching checkout quote", e)
            ResultState.Error(e.message ?: "Unknown error")
        }
    }
}
