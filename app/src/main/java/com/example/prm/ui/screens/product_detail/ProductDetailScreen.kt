package com.example.prm.ui.screens.product_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.prm.data.remote.dto.Addon
import com.example.prm.data.remote.dto.Variant
import com.example.prm.ui.theme.PurpleJobsly

@Composable
fun ProductDetailScreen(
    productId: Int,
    navController: NavHostController,
    viewModel: ProductDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = PurpleJobsly
            )
        } else if (uiState.product != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 70.dp)
            ) {
                item {
                    ProductImageSection(uiState.product!!.images, navController)
                }

                item {
                    ProductInfoSection(
                        product = uiState.product!!,
                        selectedVariantId = uiState.selectedVariantId,
                        onVariantSelect = { viewModel.selectVariant(it) }
                    )
                }

                item {
                    if (uiState.product!!.addons != null && uiState.product!!.addons!!.isNotEmpty()) {
                        AddonsSection(
                            addons = uiState.product!!.addons!!,
                            selectedAddons = uiState.selectedAddons,
                            onAddonToggle = { viewModel.toggleAddon(it) }
                        )
                    }
                }

                item {
                    ReviewsSection(
                        rating = uiState.product!!.rating,
                        reviewCount = uiState.product!!.reviewCount
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // Bottom Action Bar
            BottomActionBar(
                price = uiState.product!!.price,
                quantity = uiState.quantity,
                onQuantityChange = { viewModel.updateQuantity(it) },
                onAddToCart = { viewModel.addToCart() }
            )
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Product not found")
            }
        }
    }
}

@Composable
private fun ProductImageSection(images: List<String>, navController: NavHostController) {
    var currentImageIndex by remember { mutableStateOf(0) }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Color(0xFFF5F5F5))
        ) {
            AsyncImage(
                model = images.getOrNull(currentImageIndex) ?: images.first(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .clickable { navController.popBackStack() },
                tint = PurpleJobsly
            )
        }

        // Image Indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            images.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            if (index == currentImageIndex) PurpleJobsly else Color.Gray,
                            RoundedCornerShape(4.dp)
                        )
                        .clickable { currentImageIndex = index }
                )
            }
        }
    }
}

@Composable
private fun ProductInfoSection(
    product: com.example.prm.data.remote.dto.ProductDetail,
    selectedVariantId: Int?,
    onVariantSelect: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            product.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "★ ${product.rating}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF9800)
            )
            Text(
                "(${product.reviewCount} reviews)",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        Row(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "$${product.price}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PurpleJobsly
            )
        }

        Text(
            product.description,
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 12.dp),
            lineHeight = 18.sp
        )

        // Variants
        if (product.variants != null && product.variants!!.isNotEmpty()) {
            VariantsSection(
                variants = product.variants!!,
                selectedVariantId = selectedVariantId,
                onVariantSelect = onVariantSelect
            )
        }
    }
}

@Composable
private fun VariantsSection(
    variants: List<Variant>,
    selectedVariantId: Int?,
    onVariantSelect: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            "Options",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(
                    androidx.compose.foundation.rememberScrollState()
                ),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            variants.forEach { variant ->
                FilterChip(
                    selected = selectedVariantId == variant.id,
                    onClick = { onVariantSelect(variant.id) },
                    label = { Text("${variant.name}: ${variant.value}") }
                )
            }
        }
    }
}

@Composable
private fun AddonsSection(
    addons: List<Addon>,
    selectedAddons: List<Int>,
    onAddonToggle: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "Add-ons",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        addons.forEach { addon ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onAddonToggle(addon.id) }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(addon.name, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    Text("+$${addon.price}", fontSize = 11.sp, color = Color.Gray)
                }
                Checkbox(
                    checked = selectedAddons.contains(addon.id),
                    onCheckedChange = { onAddonToggle(addon.id) }
                )
            }
        }
    }
}

@Composable
private fun ReviewsSection(rating: Double, reviewCount: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "Reviews",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            color = Color(0xFFF5F5F5)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "$rating",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "★",
                        fontSize = 16.sp,
                        color = Color(0xFFFF9800)
                    )
                }

                Column {
                    Text(
                        "$reviewCount reviews",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "Based on customer feedback",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomActionBar(
    price: Double,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onAddToCart: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column {
                Text("Price", fontSize = 11.sp, color = Color.Gray)
                Text(
                    "$${price * quantity}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleJobsly
                )
            }

            Row(
                modifier = Modifier
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Remove,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onQuantityChange(quantity - 1) }
                )
                Text(quantity.toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onQuantityChange(quantity + 1) }
                )
            }

            Button(
                onClick = { onAddToCart() },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add")
            }
        }
    }
}

private fun Modifier.align(alignment: Alignment): Modifier = this
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    ProductDetailScreen(
        productId = 1,
        navController = androidx.navigation.compose.rememberNavController()
    )
}