package com.example.prm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductListResponse(
    @SerializedName("products")
    val products: List<ProductResp>,
    @SerializedName("pagination")
    val pagination: PaginationResp
)

data class ProductResp(
    @SerializedName("id")
    val id: String,
    @SerializedName("categoryId")
    val categoryId: String,
    @SerializedName("categoryName")
    val categoryName: String,
    @SerializedName("brandId")
    val brandId: String,
    @SerializedName("brandName")
    val brandName: String,
    @SerializedName("productName")
    val productName: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("basePrice")
    val basePrice: Double,
    @SerializedName("isActive")
    val isActive: Boolean,
    @SerializedName("images")
    val images: List<ProductImageResp>? = null,
    @SerializedName("variants")
    val variants: List<ProductVariantResp>? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null
)

data class ProductImageResp(
    @SerializedName("id")
    val id: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("altText")
    val altText: String? = null,
    @SerializedName("displayOrder")
    val displayOrder: Int,
    @SerializedName("isPrimary")
    val isPrimary: Boolean
)

data class ProductVariantResp(
    @SerializedName("id")
    val id: String,
    @SerializedName("productId")
    val productId: String,
    @SerializedName("sku")
    val sku: String,
    @SerializedName("size")
    val size: String? = null,
    @SerializedName("color")
    val color: String? = null,
    @SerializedName("weight")
    val weight: String? = null,
    @SerializedName("gripSize")
    val gripSize: String? = null,
    @SerializedName("stringTension")
    val stringTension: String? = null,
    @SerializedName("speed")
    val speed: String? = null,
    @SerializedName("material")
    val material: String? = null,
    @SerializedName("priceAdjustment")
    val priceAdjustment: Double,
    @SerializedName("finalPrice")
    val finalPrice: Double,
    @SerializedName("stockQuantity")
    val stockQuantity: Int,
    @SerializedName("isActive")
    val isActive: Boolean
)

data class PaginationResp(
    @SerializedName("currentPage")
    val currentPage: Int,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("totalPages")
    val totalPages: Int,
    @SerializedName("totalItems")
    val totalItems: Int
)

data class PaginationRequest(
    val pageNumber: Int = 1,
    val pageSize: Int = 20
)
