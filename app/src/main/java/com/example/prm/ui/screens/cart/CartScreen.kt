package com.example.prm.ui.screens.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
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
import com.example.prm.data.remote.dto.CartDataDto
import com.example.prm.data.remote.dto.CartItemDto
import com.example.prm.ui.theme.PurpleJobsly

@Composable
fun CartScreen(
    navController: NavHostController,
    viewModel: CartViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCart()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            ModernCartHeader(navController = navController)

            // Content
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = PurpleJobsly,
                            strokeWidth = 4.dp,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }

                uiState.cart == null -> {
                    EmptyCartLoginPrompt(navController = navController)
                }

                uiState.cart!!.items.isEmpty() -> {
                    EmptyCartMessage(navController = navController)
                }

                else -> {
                    CartWithItems(
                        cart = uiState.cart!!,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
        }

        // Error Message
        if (!uiState.error.isNullOrEmpty()) {
            SnackbarHost(
                hostState = remember { SnackbarHostState() },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

// ==================== HEADER ====================

@Composable
private fun ModernCartHeader(navController: NavHostController) {
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Shopping Cart",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

// ==================== EMPTY CART - LOGIN PROMPT ====================

@Composable
private fun EmptyCartLoginPrompt(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                "🛒",
                fontSize = 64.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                "Please log in to view your cart",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                "Sign in to see your shopping cart and checkout",
                fontSize = 13.sp,
                color = Color(0xFF999),
                modifier = Modifier.padding(bottom = 24.dp)
            )
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Sign In", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

// ==================== EMPTY CART MESSAGE ====================

@Composable
private fun EmptyCartMessage(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                "😢",
                fontSize = 64.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                "Your cart is empty",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                "Add some products to your cart!",
                fontSize = 13.sp,
                color = Color(0xFF999),
                modifier = Modifier.padding(bottom = 24.dp)
            )
            Button(
                onClick = { navController.navigate("products") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Continue Shopping", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

// ==================== CART WITH ITEMS ====================

@Composable
private fun CartWithItems(
    cart: CartDataDto,
    viewModel: CartViewModel,
    navController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Items List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cart.items, key = { it.id }) { item ->
                ModernCartItemCard(
                    item = item,
                    onQuantityChange = { newQty ->
                        viewModel.updateQuantity(item.id, newQty)
                    },
                    onRemove = {
                        viewModel.removeFromCart(item.id)
                    }
                )
            }
        }

        // Order Summary & Checkout
        CartSummarySection(
            cart = cart,
            navController = navController
        )
    }
}

// ==================== CART ITEM CARD ====================

@Composable
private fun ModernCartItemCard(
    item: CartItemDto,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(14.dp)),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Product Image
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.productName,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF5F5F5)),
                contentScale = ContentScale.Crop
            )

            // Product Info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = item.productName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2
                    )
                    Text(
                        text = item.size,
                        fontSize = 11.sp,
                        color = Color(0xFF999),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Price
                Text(
                    text = "${String.format("%,.2f", item.unitPrice)} đ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PurpleJobsly
                )
            }

            // Quantity & Remove
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxHeight()
            ) {
                // Quantity Controls
                Surface(
                    color = Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(6.dp)
                    ) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = "Decrease",
                            modifier = Modifier
                                .size(18.dp)
                                .clickable { if (item.quantity > 1) onQuantityChange(item.quantity - 1) },
                            tint = if (item.quantity > 1) PurpleJobsly else Color(0xFFCCC)
                        )
                        Text(
                            text = "${item.quantity}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(20.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Increase",
                            modifier = Modifier
                                .size(18.dp)
                                .clickable { onQuantityChange(item.quantity + 1) },
                            tint = PurpleJobsly
                        )
                    }
                }

                // Delete Button
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onRemove() },
                    tint = Color(0xFFFF6B6B)
                )
            }
        }
    }
}

// ==================== SUMMARY & CHECKOUT ====================

@Composable
private fun CartSummarySection(
    cart: CartDataDto,
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .shadow(elevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Divider
            HorizontalDivider(
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color(0xFFEEE)
            )

            // Summary Items
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Subtotal", fontSize = 13.sp, color = Color(0xFF666))
                Text(
                    "${String.format("%,.2f", cart.subtotal)} đ",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Shipping", fontSize = 13.sp, color = Color(0xFF666))
                Text(
                    "30,000 đ",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Discount", fontSize = 13.sp, color = Color(0xFF666))
                Text(
                    "-0 đ",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFFF6B6B)
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color(0xFFEEE)
            )

            // Total
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Total",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${String.format("%,.2f", cart.subtotal + 30000)} đ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PurpleJobsly
                )
            }

            // Checkout Button
            Button(
                onClick = { navController.navigate("checkout") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Proceed to Checkout",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}