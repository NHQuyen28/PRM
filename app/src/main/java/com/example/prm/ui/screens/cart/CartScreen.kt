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
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.material3.HorizontalDivider
import com.example.prm.data.remote.dto.CartItem
import com.example.prm.ui.theme.PurpleJobsly

@Composable
fun CartScreen(
    navController: NavHostController,
    viewModel: CartViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        CartHeader(navController)

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PurpleJobsly)
            }
        } else if (uiState.items.isEmpty()) {
            EmptyCart(navController)
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.items) { item ->
                    CartItemCard(
                        item = item,
                        onRemove = { viewModel.removeItem(item.id) }
                    )
                }

                item {
                    VoucherSection(
                        voucherInput = uiState.voucherInput,
                        appliedCode = uiState.voucherCode,
                        onVoucherChange = { viewModel.updateVoucherInput(it) },
                        onApply = { viewModel.applyVoucher() }
                    )
                }
            }

            // Summary and Checkout
            CartSummary(
                subtotal = uiState.subtotal,
                discount = uiState.discount,
                total = uiState.total,
                onCheckout = {
                    navController.navigate("checkout")
                }
            )
        }
    }
}

@Composable
private fun CartHeader(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PurpleJobsly)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = Color.White,
            modifier = Modifier
                .size(28.dp)
                .clickable { navController.popBackStack() }
        )
        Text(
            "Shopping Cart",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CartItemCard(
    item: CartItem,
    onRemove: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        color = Color(0xFFF9F9F9),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.productName,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    item.productName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    "Qty: ${item.quantity}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    "$${item.subtotal}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleJobsly
                )
            }

            Icon(
                Icons.Default.Delete,
                contentDescription = "Remove",
                tint = Color.Red,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onRemove() }
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
private fun VoucherSection(
    voucherInput: String,
    appliedCode: String?,
    onVoucherChange: (String) -> Unit,
    onApply: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            "Voucher Code",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (appliedCode != null) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                color = Color(0xFFE8F5E9)
            ) {
                Text(
                    "✓ Code applied: $appliedCode",
                    fontSize = 12.sp,
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.padding(12.dp),
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = voucherInput,
                    onValueChange = { onVoucherChange(it) },
                    placeholder = { Text("Enter code") },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF5F5F5)
                    )
                )
                Button(
                    onClick = { onApply() },
                    modifier = Modifier.height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Apply")
                }
            }
        }
    }
}

@Composable
private fun CartSummary(
    subtotal: Double,
    discount: Double,
    total: Double,
    onCheckout: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color(0xFFF9F9F9),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Subtotal", fontSize = 13.sp, color = Color.Gray)
                Text("$${String.format("%.2f", subtotal)}", fontSize = 13.sp)
            }

            if (discount > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Discount", fontSize = 13.sp, color = Color.Gray)
                    Text("-$${String.format("%.2f", discount)}", fontSize = 13.sp, color = Color(0xFF2E7D32))
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.Gray.copy(alpha = 0.3f)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(
                    "$${String.format("%.2f", total)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleJobsly
                )
            }

            Button(
                onClick = { onCheckout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Proceed to Checkout", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun EmptyCart(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Your cart is empty",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            "Add some products to get started!",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Button(
            onClick = { navController.navigate("home") },
            colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly)
        ) {
            Text("Continue Shopping")
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    CartScreen(
        navController = androidx.navigation.compose.rememberNavController()
    )
}
