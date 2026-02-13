package com.example.prm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class HomeResponse(
    @SerializedName("banners")
    val banners: List<Banner>? = null,
    @SerializedName("featured_categories")
    val featuredCategories: List<Category>? = null,
    @SerializedName("featured_products")
    val featuredProducts: List<Product>? = null
)

data class Banner(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("discount_percent")
    val discountPercent: Int? = null
)

data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("icon_url")
    val iconUrl: String
)

data class Product(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("price")
    val price: Double,
    @SerializedName("original_price")
    val originalPrice: Double? = null,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("rating")
    val rating: Double? = null,
    @SerializedName("review_count")
    val reviewCount: Int? = null,
    @SerializedName("brand_id")
    val brandId: Int? = null,
    @SerializedName("category_id")
    val categoryId: Int? = null
)

data class Brand(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("logo_url")
    val logoUrl: String? = null
)

data class ProductDetail(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("original_price")
    val originalPrice: Double? = null,
    @SerializedName("images")
    val images: List<String>,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("review_count")
    val reviewCount: Int,
    @SerializedName("variants")
    val variants: List<Variant>? = null,
    @SerializedName("addons")
    val addons: List<Addon>? = null
)

data class Variant(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("value")
    val value: String,
    @SerializedName("price_modifier")
    val priceModifier: Double? = null
)

data class Addon(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("category")
    val category: String? = null
)

data class ProductsResponse(
    @SerializedName("products")
    val products: List<Product>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int
)

data class CartItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("variant_id")
    val variantId: Int? = null,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("price")
    val price: Double,
    @SerializedName("subtotal")
    val subtotal: Double,
    @SerializedName("image_url")
    val imageUrl: String
)

data class CartResponse(
    @SerializedName("items")
    val items: List<CartItem>,
    @SerializedName("subtotal")
    val subtotal: Double,
    @SerializedName("discount")
    val discount: Double? = null,
    @SerializedName("voucher_code")
    val voucherCode: String? = null,
    @SerializedName("total")
    val total: Double
)

data class CheckoutQuoteResponse(
    @SerializedName("subtotal")
    val subtotal: Double,
    @SerializedName("shipping_fee")
    val shippingFee: Double,
    @SerializedName("discount")
    val discount: Double? = null,
    @SerializedName("tax")
    val tax: Double? = null,
    @SerializedName("total")
    val total: Double,
    @SerializedName("voucher_code")
    val voucherCode: String? = null
)

data class AvailabilityResponse(
    @SerializedName("in_stock")
    val inStock: Boolean,
    @SerializedName("quantity")
    val quantity: Int
)
