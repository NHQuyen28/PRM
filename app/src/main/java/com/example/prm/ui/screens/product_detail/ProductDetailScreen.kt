package com.example.prm.ui.screens.product_detail

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.prm.ui.theme.PurpleJobsly

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    navController: NavHostController,
    viewModel: ProductDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var quantity by remember { mutableStateOf(1) }

    // Gọi API lấy thông tin ngay khi mở màn hình
    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    // Thông báo khi thêm vào giỏ hàng thành công
    LaunchedEffect(uiState.addToCartSuccess) {
        if (uiState.addToCartSuccess) {
            Toast.makeText(context, "Added to cart!", Toast.LENGTH_SHORT).show()
            viewModel.resetCartSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Details", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("cart") }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    scrolledContainerColor = Color.White
                )
            )
        },
        bottomBar = {
            if (uiState.product != null) {
                // Lấy giá linh động: Ưu tiên giá của Variant đang chọn, nếu ko có thì lấy giá gốc
                val currentPrice = uiState.selectedVariant?.finalPrice ?: uiState.product!!.basePrice

                BottomCartBar(
                    price = currentPrice,
                    quantity = quantity,
                    onQuantityChange = { quantity = it },
                    isAdding = uiState.isAddingToCart,
                    // Không cho bấm nếu chưa chọn biến thể
                    isEnabled = uiState.selectedVariant != null,
                    onAddToCart = {
                        uiState.selectedVariant?.let { variant ->
                            viewModel.addToCart(variant.id, quantity) // Truyền ID của Biến thể
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PurpleJobsly)
            }
        } else if (uiState.errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Error: ${uiState.errorMessage}", color = Color.Red)
            }
        } else if (uiState.product != null) {
            val product = uiState.product!!
            // Tính giá hiện hành
            val currentPrice = uiState.selectedVariant?.finalPrice ?: product.basePrice

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFAFAFA))
                    .padding(paddingValues)
            ) {
                // 1. Image Carousel (Slider nhiều ảnh)
                item {
                    val images = product.images?.sortedBy { it.displayOrder }?.map { it.imageUrl }
                        ?: listOf("https://m.media-amazon.com/images/I/61r5T4X-f6L._AC_SX679_.jpg")

                    val pagerState = rememberPagerState(pageCount = { images.size })

                    Box(modifier = Modifier.fillMaxWidth().height(320.dp).background(Color.White)) {
                        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                            AsyncImage(
                                model = images[page],
                                contentDescription = product.productName,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }

                        if (images.size > 1) {
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                repeat(images.size) { index ->
                                    Box(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(if (pagerState.currentPage == index) PurpleJobsly else Color.LightGray)
                                    )
                                }
                            }
                        }
                    }
                }

                // 2. Product Info & Price
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = product.productName,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 28.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("4.8", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(" (128 reviews)", color = Color.Gray, fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "₩${String.format("%,d", currentPrice.toLong())}", // Giá update theo biến thể
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = PurpleJobsly
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "₩${String.format("%,d", (currentPrice * 1.25).toLong())}",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                textDecoration = TextDecoration.LineThrough,
                                modifier = Modifier.padding(bottom = 3.dp)
                            )
                        }
                    }
                }

                // 3. Variant Selection (Lựa chọn kích cỡ/màu sắc...)
                if (!product.variants.isNullOrEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(16.dp)
                        ) {
                            Text("Select Option", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(12.dp))

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                items(product.variants.filter { it.isActive }) { variant ->
                                    val isSelected = uiState.selectedVariant?.id == variant.id

                                    // Tạo tên nhãn hiển thị (VD: "Đỏ - 3U" hoặc "Size 40")
                                    val props = listOfNotNull(variant.color, variant.size, variant.weight).filter { it.isNotBlank() }
                                    val variantName = if (props.isNotEmpty()) props.joinToString(" - ") else variant.sku

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, if (isSelected) PurpleJobsly else Color(0xFFE0E0E0)),
                                        color = if (isSelected) PurpleJobsly.copy(alpha = 0.1f) else Color.White,
                                        modifier = Modifier.clickable { viewModel.selectVariant(variant) }
                                    ) {
                                        Text(
                                            text = variantName,
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                            color = if (isSelected) PurpleJobsly else Color.DarkGray,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 4. Description
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        Text("Product Description", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = product.description ?: "No detailed description available for this product.",
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            lineHeight = 22.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

@Composable
private fun BottomCartBar(
    price: Double,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    isAdding: Boolean,
    isEnabled: Boolean, // Thêm cờ để khoá nút nếu chưa chọn variant
    onAddToCart: () -> Unit
) {
    Surface(
        shadowElevation = 8.dp,
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Quantity Selector
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF5F5F5))
                    .padding(4.dp)
            ) {
                IconButton(
                    onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
                    modifier = Modifier.size(32.dp)
                ) { Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold) }

                Text(
                    text = quantity.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                IconButton(
                    onClick = { onQuantityChange(quantity + 1) },
                    modifier = Modifier.size(32.dp)
                ) { Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
            }

            // Add to Cart Button
            Button(
                onClick = onAddToCart,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly),
                shape = RoundedCornerShape(12.dp),
                enabled = !isAdding && isEnabled // Khoá nút nếu đang loading hoặc chưa chọn biến thể
            ) {
                if (isAdding) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else if (!isEnabled) {
                    Text("Select Option", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                } else {
                    Text(
                        "Add ₩${String.format("%,d", (price * quantity).toLong())}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}