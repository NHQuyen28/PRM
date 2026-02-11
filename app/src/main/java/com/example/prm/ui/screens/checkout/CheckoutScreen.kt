package com.example.prm.ui.screens.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.ui.tooling.preview.Preview
import com.example.prm.ui.theme.PurpleJobsly

@Composable
fun CheckoutScreen(
    navController: NavHostController,
    viewModel: CheckoutViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.orderPlaced) {
        OrderSuccessScreen(navController)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            CheckoutHeader(navController)

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PurpleJobsly)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        ShippingAddressSection(
                            fullName = uiState.fullName,
                            email = uiState.email,
                            phone = uiState.phone,
                            address = uiState.address,
                            city = uiState.city,
                            zipCode = uiState.zipCode,
                            onFullNameChange = { viewModel.updateFullName(it) },
                            onEmailChange = { viewModel.updateEmail(it) },
                            onPhoneChange = { viewModel.updatePhone(it) },
                            onAddressChange = { viewModel.updateAddress(it) },
                            onCityChange = { viewModel.updateCity(it) },
                            onZipCodeChange = { viewModel.updateZipCode(it) }
                        )
                    }

                    item {
                        OrderSummarySection(quote = uiState.quote)
                    }
                }

                CheckoutBottomBar(
                    isProcessing = uiState.isProcessing,
                    total = uiState.quote?.total ?: 0.0,
                    onPlaceOrder = { viewModel.placeOrder() }
                )
            }
        }
    }
}

@Composable
private fun CheckoutHeader(navController: NavHostController) {
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
            "Checkout",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ShippingAddressSection(
    fullName: String,
    email: String,
    phone: String,
    address: String,
    city: String,
    zipCode: String,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onZipCodeChange: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        color = Color(0xFFF9F9F9)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Shipping Address",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            OutlinedTextField(
                value = fullName,
                onValueChange = { onFullNameChange(it) },
                label = { Text("Full Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(8.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { onEmailChange(it) },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(8.dp)
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { onPhoneChange(it) },
                label = { Text("Phone Number") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(8.dp)
            )

            OutlinedTextField(
                value = address,
                onValueChange = { onAddressChange(it) },
                label = { Text("Address") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = city,
                    onValueChange = { onCityChange(it) },
                    label = { Text("City") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = zipCode,
                    onValueChange = { onZipCodeChange(it) },
                    label = { Text("Zip Code") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }
    }
}

@Composable
private fun OrderSummarySection(quote: com.example.prm.data.remote.dto.CheckoutQuoteResponse?) {
    if (quote == null) return

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        color = Color(0xFFF9F9F9)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Order Summary",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Subtotal", fontSize = 13.sp, color = Color.Gray)
                Text("$${String.format("%.2f", quote.subtotal)}", fontSize = 13.sp)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Shipping", fontSize = 13.sp, color = Color.Gray)
                Text("$${String.format("%.2f", quote.shippingFee)}", fontSize = 13.sp)
            }

            if (quote.discount != null && quote.discount > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Discount", fontSize = 13.sp, color = Color.Gray)
                    Text("-$${String.format("%.2f", quote.discount)}", fontSize = 13.sp, color = Color(0xFF2E7D32))
                }
            }

            if (quote.tax != null && quote.tax > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Tax", fontSize = 13.sp, color = Color.Gray)
                    Text("$${String.format("%.2f", quote.tax)}", fontSize = 13.sp)
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.Gray.copy(alpha = 0.3f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(
                    "$${String.format("%.2f", quote.total)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleJobsly
                )
            }
        }
    }
}

@Composable
private fun CheckoutBottomBar(
    isProcessing: Boolean,
    total: Double,
    onPlaceOrder: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color(0xFFF9F9F9),
        shadowElevation = 8.dp
    ) {
        Button(
            onClick = { onPlaceOrder() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly),
            shape = RoundedCornerShape(8.dp),
            enabled = !isProcessing
        ) {
            if (isProcessing) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    "Place Order - $${String.format("%.2f", total)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun OrderSuccessScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color(0xFF2E7D32)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Order Placed Successfully!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            "Thank you for your purchase.\nYour order will be delivered soon.",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = { navController.navigate("home") { popUpTo(0) } },
            colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Continue Shopping")
        }
    }
}
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun CheckoutScreenPreview() {
    CheckoutScreen(
        navController = androidx.navigation.compose.rememberNavController()
    )
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun OrderSuccessScreenPreview() {
    OrderSuccessScreen(
        navController = androidx.navigation.compose.rememberNavController()
    )
}