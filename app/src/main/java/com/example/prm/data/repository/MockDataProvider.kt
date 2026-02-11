package com.example.prm.data.repository

import com.example.prm.data.remote.dto.*

object MockDataProvider {
    fun getMockBanners() = listOf(
        Banner(1, "https://via.placeholder.com/400x200/FF6B6B/FFFFFF?text=Badmini+Sale+40%", "Mega Sale", 40),
        Banner(2, "https://via.placeholder.com/400x200/4ECDC4/FFFFFF?text=Summer+Collection", "Summer Collection", 30),
        Banner(3, "https://via.placeholder.com/400x200/45B7D1/FFFFFF?text=New+Arrival", "New Arrival", 20)
    )

    fun getMockCategories() = listOf(
        Category(1, "Electronics", "https://via.placeholder.com/100/FF6B6B/FFFFFF?text=Electronics"),
        Category(2, "Fashion", "https://via.placeholder.com/100/4ECDC4/FFFFFF?text=Fashion"),
        Category(3, "Home & Garden", "https://via.placeholder.com/100/45B7D1/FFFFFF?text=Home"),
        Category(4, "Sports", "https://via.placeholder.com/100/F7DC6F/FFFFFF?text=Sports")
    )

    fun getMockBrands() = listOf(
        Brand(1, "Apple", "https://via.placeholder.com/80/000000/FFFFFF?text=Apple"),
        Brand(2, "Samsung", "https://via.placeholder.com/80/1428A0/FFFFFF?text=Samsung"),
        Brand(3, "Sony", "https://via.placeholder.com/80/000000/FFFFFF?text=Sony"),
        Brand(4, "Nike", "https://via.placeholder.com/80/111111/FFFFFF?text=Nike")
    )

    fun getMockProducts() = listOf(
        Product(1, "Wireless Headphones", "High-quality sound", 89.99, 129.99, "https://via.placeholder.com/200/FF6B6B/FFFFFF?text=Headphones", 4.5, 128),
        Product(2, "Smart Watch", "Latest technology", 199.99, 299.99, "https://via.placeholder.com/200/4ECDC4/FFFFFF?text=Smart+Watch", 4.8, 256),
        Product(3, "USB-C Cable", "Fast charging", 12.99, 19.99, "https://via.placeholder.com/200/45B7D1/FFFFFF?text=USB-C+Cable", 4.2, 89),
        Product(4, "Portable Speaker", "Bluetooth 5.0", 49.99, 79.99, "https://via.placeholder.com/200/F7DC6F/FFFFFF?text=Speaker", 4.6, 145),
        Product(5, "Phone Stand", "Aluminum alloy", 15.99, 24.99, "https://via.placeholder.com/200/95E1D3/FFFFFF?text=Phone+Stand", 4.3, 67),
        Product(6, "Screen Protector", "Tempered glass", 7.99, 14.99, "https://via.placeholder.com/200/C7CEEA/FFFFFF?text=Protector", 4.4, 234),
        Product(7, "Phone Case", "Protective design", 19.99, 39.99, "https://via.placeholder.com/200/FF6B6B/FFFFFF?text=Phone+Case", 4.7, 189),
        Product(8, "Charging Dock", "Fast wireless charging", 34.99, 59.99, "https://via.placeholder.com/200/4ECDC4/FFFFFF?text=Dock", 4.5, 112)
    )

    fun getMockHomeResponse() = HomeResponse(
        banners = getMockBanners(),
        featuredCategories = getMockCategories(),
        featuredProducts = getMockProducts()
    )

    fun getMockProductDetail(productId: Int): ProductDetail {
        val product = getMockProducts().find { it.id == productId }
            ?: getMockProducts().first()
        return ProductDetail(
            id = product.id,
            name = product.name,
            description = "This is a premium quality ${product.name}. It comes with excellent features and warranty. Perfect for everyday use.",
            price = product.price,
            originalPrice = product.originalPrice,
            images = listOf(
                product.imageUrl,
                "https://via.placeholder.com/400/95E1D3/FFFFFF?text=${product.name}+2",
                "https://via.placeholder.com/400/F7DC6F/FFFFFF?text=${product.name}+3"
            ),
            rating = product.rating ?: 4.5,
            reviewCount = product.reviewCount ?: 100,
            variants = listOf(
                Variant(1, "Color", "Black", 0.0),
                Variant(2, "Color", "White", 0.0),
                Variant(3, "Size", "S", 5.0)
            ),
            addons = listOf(
                Addon(1, "Extended Warranty", 19.99, "warranty"),
                Addon(2, "Screen Protector", 9.99, "protection"),
                Addon(3, "Premium Packaging", 4.99, "packaging")
            )
        )
    }

    fun getMockCartItems() = listOf(
        CartItem(1, 1, "Wireless Headphones", 1, 2, 89.99, 179.98, "https://via.placeholder.com/100/FF6B6B/FFFFFF?text=Headphones"),
        CartItem(2, 2, "Smart Watch", 2, 1, 199.99, 199.99, "https://via.placeholder.com/100/4ECDC4/FFFFFF?text=Watch")
    )

    fun getMockCartResponse() = CartResponse(
        items = getMockCartItems(),
        subtotal = 379.97,
        discount = 0.0,
        voucherCode = null,
        total = 379.97
    )

    fun getMockCheckoutQuote() = CheckoutQuoteResponse(
        subtotal = 379.97,
        shippingFee = 9.99,
        discount = 37.99,
        tax = 5.0,
        total = 356.97,
        voucherCode = "SAVE10"
    )
}
