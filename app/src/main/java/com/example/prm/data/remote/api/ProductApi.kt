package com.example.prm.data.remote.api

import com.example.prm.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ProductApi {
    @GET("/api/v1/home")
    suspend fun getHome(): Response<HomeResponse>

    @GET("/api/v1/categories")
    suspend fun getCategories(): Response<List<Category>>

    @GET("/api/v1/brands")
    suspend fun getBrands(): Response<List<Brand>>

    @GET("/api/v1/products")
    suspend fun getProducts(
        @Query("search") search: String? = null,
        @Query("category_id") categoryId: Int? = null,
        @Query("brand_id") brandId: Int? = null,
        @Query("sort") sort: String? = null,
        @Query("page") page: Int? = 1,
        @Query("per_page") perPage: Int? = 20
    ): Response<ProductsResponse>

    @GET("/api/v1/products/{productId}")
    suspend fun getProductDetail(
        @Path("productId") productId: Int
    ): Response<ProductDetail>

    @GET("/api/v1/products/{productId}/variants")
    suspend fun getProductVariants(
        @Path("productId") productId: Int
    ): Response<List<Variant>>

    @GET("/api/v1/variants/{variantId}/availability")
    suspend fun checkAvailability(
        @Path("variantId") variantId: Int,
        @Query("quantity") quantity: Int
    ): Response<AvailabilityResponse>

    @GET("/api/v1/addons")
    suspend fun getAddons(): Response<List<Addon>>

    @GET("/api/v1/cart")
    suspend fun getCart(): Response<CartResponse>

    @POST("/api/v1/cart/items")
    suspend fun addToCart(
        @Body request: AddToCartRequest
    ): Response<CartItem>

    @PUT("/api/v1/cart/items/{itemId}")
    suspend fun updateCartItem(
        @Path("itemId") itemId: Int,
        @Body request: UpdateCartItemRequest
    ): Response<CartItem>

    @DELETE("/api/v1/cart/items/{itemId}")
    suspend fun removeFromCart(
        @Path("itemId") itemId: Int
    ): Response<Void>

    @POST("/api/v1/cart/apply-voucher")
    suspend fun applyVoucher(
        @Body request: ApplyVoucherRequest
    ): Response<CartResponse>

    @DELETE("/api/v1/cart/voucher")
    suspend fun removeVoucher(): Response<CartResponse>

    @POST("/api/v1/checkout/quote")
    suspend fun getCheckoutQuote(
        @Body request: CheckoutQuoteRequest
    ): Response<CheckoutQuoteResponse>
}

data class AddToCartRequest(
    val productId: Int,
    val variantId: Int? = null,
    val quantity: Int,
    val addonIds: List<Int> = emptyList()
)

data class UpdateCartItemRequest(
    val quantity: Int
)

data class ApplyVoucherRequest(
    val code: String
)

data class CheckoutQuoteRequest(
    val itemIds: List<Int>,
    val voucherCode: String? = null
)
