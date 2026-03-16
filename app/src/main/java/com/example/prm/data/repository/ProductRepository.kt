package com.example.prm.data.repository

import android.util.Log
import com.example.prm.data.remote.RetrofitClient
import com.example.prm.data.remote.api.*
import com.example.prm.data.remote.dto.ProductResp
import com.example.prm.data.remote.dto.ProductVariantResp
import com.example.prm.data.remote.dto.ProductListResponse
import com.example.prm.utils.ResultState

class ProductRepository {
    private val productApi = RetrofitClient.createService(ProductApi::class.java)
    private val TAG = "ProductRepository"

    suspend fun getProducts(
        page: Int = 1,
        pageSize: Int = 10,
        search: String? = null,
        categoryId: String? = null,
        brandId: String? = null,
        sortBy: String? = null,
        isAscending: Boolean = true
    ): ResultState<ProductListResponse> {
        return try {
            val response = productApi.getProducts(page, pageSize, search, categoryId, brandId, sortBy, isAscending)
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    ResultState.Success(apiResponse.data)
                } else {
                    ResultState.Error(apiResponse.message ?: "Unknown error")
                }
            } else {
                ResultState.Error("Failed to fetch products: HTTP ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting products", e)
            ResultState.Error(e.message ?: "Network error")
        }
    }

    suspend fun getProductById(id: String): ResultState<ProductResp> {
        return try {
            val response = productApi.getProductById(id)
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    ResultState.Success(apiResponse.data)
                } else {
                    ResultState.Error(apiResponse.message ?: "Product not found")
                }
            } else {
                ResultState.Error("Failed to get product: HTTP ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting product details", e)
            ResultState.Error(e.message ?: "Network error")
        }
    }

    suspend fun createProduct(
        categoryId: String,
        brandId: String,
        productName: String,
        slug: String?,
        description: String?,
        basePrice: Double,
        imageUrl: String?
    ): ResultState<ProductResp> {
        return try {
            val imagesList = if (!imageUrl.isNullOrBlank()) listOf(imageUrl) else null
            val request = CreateProductRequest(categoryId, brandId, productName, slug, description, basePrice, true, null, imagesList)
            val response = productApi.createProduct(request)
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    ResultState.Success(apiResponse.data)
                } else {
                    ResultState.Error(apiResponse.message ?: "Failed to create product")
                }
            } else {
                ResultState.Error("HTTP Error ${response.code()}")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Network error")
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
        isActive: Boolean,
        imageUrl: String?
    ): ResultState<ProductResp> {
        return try {
            val imagesList = if (!imageUrl.isNullOrBlank()) listOf(imageUrl) else null
            // Đã nhét `id` vào request thay vì Path
            val request = UpdateProductRequest(id, categoryId, brandId, productName, slug, description, basePrice, isActive, imagesList)
            val response = productApi.updateProduct(request)
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    ResultState.Success(apiResponse.data)
                } else {
                    ResultState.Error(apiResponse.message ?: "Failed to update product")
                }
            } else {
                ResultState.Error("HTTP Error ${response.code()}")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Network error")
        }
    }

    suspend fun deleteProduct(id: String): ResultState<Boolean> {
        return try {
            val response = productApi.deleteProduct(id)
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success) ResultState.Success(true)
                else ResultState.Error(apiResponse.message ?: "Failed to delete product")
            } else {
                ResultState.Error("HTTP Error ${response.code()}")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Network error")
        }
    }

    suspend fun addVariant(
        productId: String, sku: String, size: String?, color: String?, weight: String?,
        gripSize: String?, stringTension: String?, stockQuantity: Int, priceAdjustment: Double
    ): ResultState<ProductVariantResp> {
        return try {
            val req = CreateProductVariantRequest(sku, size, color, weight, gripSize, stringTension, null, null, priceAdjustment, stockQuantity, true)
            val response = productApi.addProductVariant(productId, req)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success && body.data != null) ResultState.Success(body.data)
                else ResultState.Error(body.message ?: "Lỗi thêm biến thể")
            } else {
                ResultState.Error("Lỗi HTTP ${response.code()}")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Lỗi mạng")
        }
    }

    suspend fun updateVariant(
        variantId: String, size: String?, color: String?, stockQuantity: Int, priceAdjustment: Double, isActive: Boolean
    ): ResultState<ProductVariantResp> {
        return try {
            // Đã nhét `id` vào request thay vì Path
            val req = UpdateProductVariantRequest(variantId, size, color, priceAdjustment, stockQuantity, isActive)
            val response = productApi.updateProductVariant(req)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success && body.data != null) ResultState.Success(body.data)
                else ResultState.Error(body.message ?: "Lỗi cập nhật biến thể")
            } else {
                ResultState.Error("Lỗi HTTP ${response.code()}")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Lỗi mạng")
        }
    }
}