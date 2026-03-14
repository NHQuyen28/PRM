package com.example.prm.ui.screens.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.prm.ui.theme.PurpleJobsly

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavHostController,
    viewModel: CheckoutViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    var showAddressDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadAddresses()
        viewModel.loadCart()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
    ) {

        // HEADER

        ModernCartHeader(navController = navController)

        Spacer(modifier = Modifier.height(12.dp))

        // ADDRESS CARD
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),

            shape = RoundedCornerShape(14.dp),

            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50) // xanh đẹp
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        "Shipping Address",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                uiState.selectedAddress?.let { address ->

                    Text(
                        address.recipientName,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        address.phone,
                        color = Color.Gray
                    )

                    Text(
                        address.fullAddress,
                        color = Color.Gray
                    )

                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = { showAddressDialog = true }
                ) {
                    Text(
                        "Change Address",
                        color = Color(0xFF2196F3)
                    )
                }

            }

        }

        uiState.cart?.let { cart ->

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),

                shape = RoundedCornerShape(14.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        "Order Items",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    cart.items.forEach { item ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),

                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {

                                Text(item.productName)

                                Text(
                                    "x${item.quantity}",
                                    color = Color.Gray
                                )

                            }

                            Text(
                                String.format("%,.2f đ", item.unitPrice * item.quantity)
                            )

                        }

                    }

                    Divider(modifier = Modifier.padding(vertical = 10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            "Total",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = String.format("%,.2f đ", cart.subtotal),
                            color = Color(0xFF4CAF50),
                            style = MaterialTheme.typography.titleMedium
                        )

                    }

                }

            }

        }

        Spacer(modifier = Modifier.weight(1f))

        // PLACE ORDER BUTTON
        Button(
            onClick = {
                viewModel.placeOrder()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(55.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            )
        ) {

            Text(
                "Place Order",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

        }

    }

    // ADDRESS SELECTOR
    if (showAddressDialog) {

        AddressSelectorDialog(
            addresses = uiState.addresses,
            onSelect = {
                viewModel.selectAddress(it)
                showAddressDialog = false
            },
            onDismiss = {
                showAddressDialog = false
            }
        )

    }

}

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
                    "Checkout",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}