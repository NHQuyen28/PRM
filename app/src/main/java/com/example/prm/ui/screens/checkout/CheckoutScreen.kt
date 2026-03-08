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
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
        ModernOrderSuccessScreen(navController)
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA))
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                ModernCheckoutHeader(navController)

                if (uiState.isLoading) {
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
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            ModernShippingAddressSection(
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
                            ModernOrderSummarySection(quote = uiState.quote)
                        }

                        item {
                            PaymentMethodSection()
                        }

                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    ModernCheckoutBottomBar(
                        isProcessing = uiState.isProcessing,
                        total = uiState.quote?.total ?: 0.0,
                        onPlaceOrder = { viewModel.placeOrder() }
                    )
                }
            }
        }
    }
}

// ==================== HEADER ====================

@Composable
private fun ModernCheckoutHeader(navController: NavHostController) {
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
                Column {
                    Text(
                        "Order Checkout",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        "Complete your purchase",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

// ==================== SHIPPING ADDRESS ====================

@Composable
private fun ModernShippingAddressSection(
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
            .clip(RoundedCornerShape(14.dp))
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(14.dp)),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                "📍 Shipping Address",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            ModernTextField(
                value = fullName,
                onValueChange = onFullNameChange,
                label = "Full Name",
                placeholder = "John Doe"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ModernTextField(
                value = email,
                onValueChange = onEmailChange,
                label = "Email",
                placeholder = "your@email.com"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ModernTextField(
                value = phone,
                onValueChange = onPhoneChange,
                label = "Phone Number",
                placeholder = "+84 912 345 678"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ModernTextField(
                value = address,
                onValueChange = onAddressChange,
                label = "Street Address",
                placeholder = "123 Main Street"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ModernTextField(
                    value = city,
                    onValueChange = onCityChange,
                    label = "City",
                    placeholder = "Hanoi",
                    modifier = Modifier.weight(1f)
                )

                ModernTextField(
                    value = zipCode,
                    onValueChange = onZipCodeChange,
                    label = "Zip Code",
                    placeholder = "10000",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 13.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurpleJobsly,
                unfocusedBorderColor = Color(0xFFDDD),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color(0xFFFAFAFA)
            ),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(fontSize = 13.sp)
        )
    }
}

// ==================== ORDER SUMMARY ====================

@Composable
private fun ModernOrderSummarySection(quote: Any?) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(14.dp)),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                "📦 Order Summary",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Items Count
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Items", fontSize = 13.sp, color = Color(0xFF666))
                Text("1 item", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }

            // Subtotal
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Subtotal", fontSize = 13.sp, color = Color(0xFF666))
                Text(
                    "₩1,000,000",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Shipping
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Shipping", fontSize = 13.sp, color = Color(0xFF666))
                Text(
                    "₩30,000",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Discount
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Discount", fontSize = 13.sp, color = Color(0xFF666))
                Text(
                    "-₩0",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFFF6B6B)
                )
            }

            HorizontalDivider(color = Color(0xFFEEE), modifier = Modifier.padding(bottom = 16.dp))

            // Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Total",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "₩1,030,000",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PurpleJobsly
                )
            }
        }
    }
}

// ==================== PAYMENT METHOD ====================

@Composable
private fun PaymentMethodSection() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(14.dp)),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                "💳 Payment Method",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Credit Card Option (Selected)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
                color = Color(0xFFF0F7FF)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RadioButton(
                        selected = true,
                        onClick = {},
                        colors = RadioButtonDefaults.colors(selectedColor = PurpleJobsly)
                    )
                    Column {
                        Text(
                            "Credit Card",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "Visa, Mastercard, Amex",
                            fontSize = 11.sp,
                            color = Color(0xFF999)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // COD Option
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
                color = Color(0xFFF9F9F9)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RadioButton(
                        selected = false,
                        onClick = {},
                        colors = RadioButtonDefaults.colors(selectedColor = PurpleJobsly)
                    )
                    Column {
                        Text(
                            "Cash on Delivery",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "Pay when you receive",
                            fontSize = 11.sp,
                            color = Color(0xFF999)
                        )
                    }
                }
            }
        }
    }
}

// ==================== BOTTOM BAR ====================

@Composable
private fun ModernCheckoutBottomBar(
    isProcessing: Boolean,
    total: Double,
    onPlaceOrder: () -> Unit
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total to Pay", fontSize = 13.sp, color = Color(0xFF666))
                Text(
                    "₩${String.format("%,d", total.toInt())}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PurpleJobsly
                )
            }

            Button(
                onClick = onPlaceOrder,
                enabled = !isProcessing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        "Place Order",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

// ==================== SUCCESS SCREEN ====================

@Composable
private fun ModernOrderSuccessScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(PurpleJobsly, Color(0xFF7C3AED))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(40.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Order Placed Successfully!",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Your order has been confirmed.\nYou'll receive updates via email.",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 13.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Back to Home",
                    color = PurpleJobsly,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}