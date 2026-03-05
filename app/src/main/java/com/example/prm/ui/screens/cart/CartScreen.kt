package com.example.prm.ui.screens.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.prm.data.remote.dto.cart.CartItemDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavHostController,
    viewModel: CartViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

    }

    LaunchedEffect(Unit) {
        viewModel.loadCart()
    }

    Scaffold(

        topBar = {

            TopAppBar(
                title = {
                    Text(
                        "My Cart",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },

                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }

    ) { paddingValues ->

        if (uiState.cart == null) {

            // CHƯA LOGIN -> HIỆN LOGIN BUTTON
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text("You are not logged in.")

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { navController.navigate("login") },
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("Login")
                    }

                }

            }

        } else {

            // ĐÃ LOGIN -> HIỆN CART
            val cart = uiState.cart!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {

                    items(cart.items,
                        key = { it.id }) { item ->
                        CartItemCard(item, viewModel)
                    }

                }

                HorizontalDivider()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "Total:",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "${cart.subtotal} đ",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    Text("Checkout")

                }

            }
        }
    }
}


@Composable
fun CartItemCard(item: CartItemDto,
                 viewModel: CartViewModel) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(12.dp)
    ) {

        Row(
            modifier = Modifier.padding(12.dp)
        ) {

            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.productName,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = item.productName,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = item.brandName,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Size: ${item.size} | Color: ${item.color}",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text("${item.unitPrice} đ")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        if (item.quantity > 1) {
                            viewModel.updateQuantity(
                                item.id,
                                item.quantity - 1
                            )
                        }
                    }
                ) {
                    Text("-")
                }

                Text("${item.quantity}")

                IconButton(
                    onClick = {
                        viewModel.updateQuantity(
                            item.id,
                            item.quantity + 1
                        )
                    }
                ) {
                    Text("+")
                }
            }
        }
    }
}