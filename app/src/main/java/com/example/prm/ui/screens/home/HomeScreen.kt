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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.prm.ui.theme.PurpleJobsly
import com.example.prm.ui.theme.PRMTheme

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isLoggedOut by viewModel.isLoggedOut.collectAsState()

    LaunchedEffect(Unit) {
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
            .background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            item {
                HomeHeader(navController, onLogout = { viewModel.logout() })
            }

            // Banner Carousel
            item {
                if (uiState.banners.isNotEmpty()) {
                    BannerCarousel(uiState.banners)
                }
            }

            // Search Bar
            item {
                SearchBar(navController)
            }

            // Categories
            item {
                if (uiState.categories.isNotEmpty()) {
                    CategorySection(uiState.categories, navController)
                }
            }

            // Featured Products
            item {
                FeaturedProductsSection(
                    products = uiState.products,
                    isLoading = uiState.isLoading,
                    navController = navController,
                    onAddToCart = { product -> viewModel.addToCart(product.id) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Loading indicator
        if (uiState.isLoading && uiState.products.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = PurpleJobsly
            )
        }
    }
}

@Composable
private fun HomeHeader(
    navController: NavHostController,
    onLogout: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PurpleJobsly)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "Badmini",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Welcome back!",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp
            )
        }
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onLogout,
                modifier = Modifier.height(36.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))
            ) {
                Text("Logout", fontSize = 12.sp, color = Color.White)
            }
            
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
    }
}

@Composable
private fun BannerCarousel(banners: List<Banner>) {
    var currentIndex by remember { mutableStateOf(0) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(banners) { banner ->
            BannerCard(banner)
        }
    }
}

@Composable
private fun BannerCard(banner: Banner) {
    Box(
        modifier = Modifier
            .width(320.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(PurpleJobsly),
        contentAlignment = Alignment.BottomStart
    ) {
        AsyncImage(
            model = banner.imageUrl,
            contentDescription = banner.title,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.4f))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    banner.title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                if (banner.discountPercent != null) {
                    Text(
                        "Save ${banner.discountPercent}%",
                        color = Color(0xFFFFD700),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchBar(navController: NavHostController) {
    TextField(
        value = "",
        onValueChange = {},
        placeholder = { Text("Search products...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                navController.navigate("products?search=")
            },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFF5F5F5),
            focusedContainerColor = Color(0xFFF5F5F5)
        )
    )
}

@Composable
private fun CategorySection(categories: List<Category>, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "Categories",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryCard(
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
private fun CategoryCard(category: Category, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(12.dp)),
            color = Color(0xFFF5F5F5)
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
                .padding(top = 8.dp)
                .fillMaxWidth(),
            maxLines = 1
        )
    }
}

@Composable
private fun FeaturedProductsSection(
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
        Text(
            "Featured Products",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products) { product ->
                ProductCard(
                    product = product,
                    onProductClick = { navController.navigate("product_detail/${product.id}") },
                    onAddClick = { onAddToCart(product) }
                )
            }
        }

        Button(
            onClick = { navController.navigate("products") },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly)
        ) {
            Text("View All Products")
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    onProductClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onProductClick() },
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    product.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )

                if (product.rating != null) {
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "★ ${product.rating}",
                            fontSize = 11.sp,
                            color = Color(0xFFFF9800)
                        )
                        Text(
                            "(${product.reviewCount})",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                }

                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        "$${product.price}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = PurpleJobsly
                    )
                    if (product.originalPrice != null) {
                        Text(
                            "$${product.originalPrice}",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            style = LocalTextStyle.current.copy(
                                textDecoration = TextDecoration.LineThrough
                            )
                        )
                    }
                }

                Button(
                    onClick = { onAddClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Text("Add", fontSize = 11.sp)
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
