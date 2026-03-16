package com.example.prm.ui.screens.home

import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.prm.data.remote.dto.Banner
import com.example.prm.data.remote.dto.Category
import com.example.prm.data.remote.dto.Product
import com.example.prm.data.session.SessionManager
import com.example.prm.ui.theme.PurpleJobsly
import com.example.prm.ui.theme.PRMTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import kotlinx.coroutines.delay
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.MarkerState

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isLoggedOut by viewModel.isLoggedOut.collectAsState()
    val context = LocalContext.current
    val sessionManager = SessionManager(context)

    LaunchedEffect(Unit) {
        // Check if user is admin and redirect
        if (sessionManager.isAdmin()) {
            navController.navigate("admin_dashboard") {
                popUpTo("home") { inclusive = true }
            }
        }
        viewModel.loadHome()
    }

    // Handle logout navigation
    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Modern Header with Gradient
            item {
                HomeHeaderModern(navController = navController)
            }

            // Banner Carousel with Auto-scroll
            item {
                if (uiState.banners.isNotEmpty()) {
                    AnimatedBannerCarousel(banners = uiState.banners)
                }
            }

            // Premium Search Bar
            item {
                PremiumSearchBar(navController = navController)
            }

            // Flash Sale / Special Offer
            item {
                SpecialOfferSection()
            }

            // Categories Section
            item {
                if (uiState.categories.isNotEmpty()) {
                    CategoryShowcase(categories = uiState.categories, navController = navController)
                }
            }

            // Featured/Best Selling Products
            item {
                BestSellingProductsSection(
                    products = uiState.products,
                    isLoading = uiState.isLoading,
                    navController = navController,
                    // TẠM THỜI BỎ TRỐNG LỆNH NÀY VÌ ĐÃ ẨN NÚT ADD TO CART
                    onAddToCart = { /* Không làm gì cả */ }
                )
            }

            // View All Products CTA
            item {
                ViewAllProductsCTA(navController = navController)
            }

            // Shop Location Map
            item {
                ShopLocationMap()
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }

        // Loading indicator
        if (uiState.isLoading && uiState.products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = PurpleJobsly,
                    strokeWidth = 4.dp
                )
            }
        }
    }
}

// ==================== HEADER SECTION ====================

@Composable
private fun HomeHeaderModern(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(PurpleJobsly, Color(0xFF7C3AED))
                )
            )
            .padding(20.dp)
    ) {
        Column {
            // Top Row: Logo & Icons
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "⛳ Badmini",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        "Badminton Equipment Store",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Notifications
                    BadgedBox(
                        badge = {
                            Badge(
                                modifier = Modifier.offset(x = (-8).dp, y = 8.dp),
                                containerColor = Color(0xFFFF6B6B)
                            )
                        }
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White,
                            modifier = Modifier
                                .size(28.dp)
                                .clickable { }
                        )
                    }
                    
                    // Cart
                    BadgedBox(
                        badge = {
                            Badge(
                                modifier = Modifier.offset(x = (-8).dp, y = 8.dp),
                                containerColor = Color(0xFFFF6B6B)
                            ) {
                                Text("0", fontSize = 10.sp)
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "Cart",
                            tint = Color.White,
                            modifier = Modifier
                                .size(28.dp)
                                .clickable {
                                    navController.navigate("cart")
                                }
                        )
                    }

                    // Profile
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                navController.navigate("profile")
                            }
                    )
                }
            }
        }
    }
}

// ==================== BANNER CAROUSEL ====================

@Composable
private fun AnimatedBannerCarousel(banners: List<Banner>) {
    val pagerState = rememberPagerState(pageCount = { banners.size })
    
    // Auto-scroll effect
    LaunchedEffect(Unit) {
        while (true) {
            delay(4000) // 4 seconds
            pagerState.animateScrollToPage(
                (pagerState.currentPage + 1) % banners.size
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
        ) { page ->
            ModernBannerCard(banner = banners[page])
        }

        // Indicator dots
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(banners.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (index == pagerState.currentPage) 12.dp else 8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (index == pagerState.currentPage) PurpleJobsly else Color(0xFFDDD)
                        )
                )
            }
        }
    }
}

@Composable
private fun ModernBannerCard(banner: Banner) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomStart
    ) {
        AsyncImage(
            model = banner.imageUrl,
            contentDescription = banner.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Text(
                    banner.title,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                if (banner.discountPercent != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFF6B6B), shape = RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                "Save ${banner.discountPercent}%",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==================== SEARCH BAR ====================

@Composable
private fun PremiumSearchBar(navController: NavHostController) {
    TextField(
        value = "",
        onValueChange = {},
        placeholder = { Text("Search racket, shoes...", fontSize = 14.sp) },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = Color(0xFF999),
                modifier = Modifier.size(22.dp)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp))
            .clickable {
                navController.navigate("products?search=")
            },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        singleLine = true
    )
}

// ==================== SPECIAL OFFER SECTION ====================

@Composable
private fun SpecialOfferSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .background(
                brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                    listOf(Color(0xFFFF6B6B), Color(0xFFFF8E72))
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "🔥 Flash Sale Live!",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Up to 40% OFF on selected items",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp
                )
            }
            Button(
                onClick = { },
                modifier = Modifier.height(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Shop Now", color = Color(0xFFFF6B6B), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}

// ==================== CATEGORY SHOWCASE ====================

@Composable
private fun CategoryShowcase(categories: List<Category>, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Shop by Category",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "View All",
                fontSize = 12.sp,
                color = PurpleJobsly,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    navController.navigate("products")
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories.take(5)) { category ->
                ModernCategoryCard(
                    category = category,
                    onClick = {
                        navController.navigate("products?categoryId=${category.id}")
                    }
                )
            }
        }
    }
}

@Composable
private fun ModernCategoryCard(category: Category, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(90.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(14.dp))
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(14.dp)),
            color = Color.White
        ) {
            AsyncImage(
                model = category.iconUrl,
                contentDescription = category.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            category.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            maxLines = 2,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

// ==================== BEST SELLING PRODUCTS ====================

@Composable
private fun BestSellingProductsSection(
    products: List<Product>,
    isLoading: Boolean,
    navController: NavHostController,
    onAddToCart: (Product) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Best Selling",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "See More",
                fontSize = 12.sp,
                color = PurpleJobsly,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    navController.navigate("products")
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = PurpleJobsly
            )
        } else {
            // SỬ DỤNG CHUNKED ĐỂ TẠO GRID 2 CỘT
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Lấy tối đa 6 sản phẩm (hoặc bạn có thể bỏ .take(6) để hiện hết)
                val displayProducts = products.take(6)
                val chunkedProducts = displayProducts.chunked(2)

                chunkedProducts.forEach { rowProducts ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowProducts.forEach { product ->
                            Box(modifier = Modifier.weight(1f)) {
                                ModernProductGridCard(
                                    product = product,
                                    onProductClick = { navController.navigate("product_detail/${product.id}") },
                                    onAddClick = { onAddToCart(product) }
                                )
                            }
                        }
                        // Nếu hàng cuối cùng chỉ có 1 sản phẩm (lẻ), thêm 1 khoảng trống để đẩy nó sang trái
                        if (rowProducts.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernProductGridCard(
    product: Product,
    onProductClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onProductClick() }
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(14.dp)),
        color = Color.White
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFFF5F5F5))
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .background(Color(0xFFFF6B6B), shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        "-20%",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Wishlist
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = "Add to Wishlist",
                    tint = Color(0xFFFF6B6B).copy(alpha = 0.3f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(24.dp)
                        .clickable { }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    product.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("⭐ 4.8", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Text("(128)", fontSize = 10.sp, color = Color(0xFF999))
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Price
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "${String.format(java.util.Locale("vi", "VN"), "%,d", product.price.toInt())} đ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PurpleJobsly
                    )
                    Text(
                        "${String.format(java.util.Locale("vi", "VN"), "%,d", (product.price * 1.25).toInt())} đ",
                        fontSize = 12.sp,
                        color = Color(0xFF999),
                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))


            }
        }
    }
}

// ==================== VIEW ALL CTA ====================

@Composable
private fun ViewAllProductsCTA(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(Color(0xFF7C3AED), PurpleJobsly)
                ),
                shape = RoundedCornerShape(14.dp)
            )
            .clip(RoundedCornerShape(14.dp))
            .clickable {
                navController.navigate("products")
            }
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "View All Products →",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

// ==================== SHOP LOCATION ====================

@Composable
private fun ShopLocationMap() {
    val shopLatitude = 10.7769  // Saigon coordinates
    val shopLongitude = 106.6869
    val location = LatLng(shopLatitude, shopLongitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 16f)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Title
        Text(
            "📍 Our Shop Location",
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PurpleJobsly,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Map Container
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(16.dp))
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp)),
            color = Color.White
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = location),
                    title = "Badmini Shop",
                    snippet = "Badminton Equipment Store\nHo Chi Minh City, Vietnam"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Shop Info Card
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .shadow(elevation = 2.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text("🏪", fontSize = 24.sp)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Badmini Shop",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PurpleJobsly
                        )
                        Text(
                            "Ho Chi Minh City, Vietnam",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                Divider(color = Color(0xFFEEE))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📞", fontSize = 18.sp)
                    Text(
                        "+84 (0) 1234-5678",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📧", fontSize = 18.sp)
                    Text(
                        "support@badmini.com",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🕒", fontSize = 18.sp)
                    Text(
                        "9:00 AM - 6:00 PM (Daily)",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    PRMTheme {
        HomeScreen(
            navController = androidx.navigation.compose.rememberNavController()
        )
    }
}
