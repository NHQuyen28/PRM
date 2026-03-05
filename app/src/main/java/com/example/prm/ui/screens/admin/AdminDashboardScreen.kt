package com.example.prm.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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
import com.example.prm.data.remote.dto.ProductResp
import com.example.prm.ui.theme.PurpleJobsly

@Composable
fun AdminDashboardScreen(
    navController: NavHostController,
    viewModel: AdminDashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(AdminTab.PRODUCTS) }

    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        AdminHeader(
            onLogout = {
                navController.navigate("login") {
                    popUpTo("admin_dashboard") { inclusive = true }
                }
            }
        )

        // Tab Navigation
        AdminTabRow(
            selectedTab = selectedTab,
            onTabSelect = { selectedTab = it }
        )

        // Content
        when (selectedTab) {
            AdminTab.PRODUCTS -> AdminProductsTab(
                navController = navController,
                viewModel = viewModel,
                uiState = uiState
            )
            AdminTab.ORDERS -> AdminOrdersTab(viewModel = viewModel)
            AdminTab.ANALYTICS -> AdminAnalyticsTab()
        }
    }
}

@Composable
private fun AdminHeader(onLogout: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PurpleJobsly)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Admin Dashboard",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = onLogout) {
            Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.White)
        }
    }
}

@Composable
private fun AdminTabRow(
    selectedTab: AdminTab,
    onTabSelect: (AdminTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AdminTab.entries.forEach { tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = { onTabSelect(tab) },
                text = { Text(tab.label) }
            )
        }
    }
}

@Composable
private fun AdminProductsTab(
    navController: NavHostController,
    viewModel: AdminDashboardViewModel,
    uiState: AdminDashboardUiState
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Add Product Button
        Button(
            onClick = { navController.navigate("admin_add_product") },
            modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Product", color = Color.White)
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PurpleJobsly)
            }
        } else if (uiState.products.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No products found", fontSize = 16.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.products) { product ->
                    AdminProductCard(
                        product = product,
                        onEdit = { navController.navigate("admin_edit_product/${product.id}") },
                        onDelete = { viewModel.deleteProduct(product.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminProductCard(
    product: ProductResp,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF9F9F9))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.productName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "₫${product.basePrice}",
                    fontSize = 12.sp,
                    color = PurpleJobsly,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Variants: ${product.variants?.size ?: 0}",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = PurpleJobsly)
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}

@Composable
private fun AdminOrdersTab(viewModel: AdminDashboardViewModel) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Orders management coming soon", fontSize = 16.sp)
    }
}

@Composable
private fun AdminAnalyticsTab() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Analytics dashboard coming soon", fontSize = 16.sp)
    }
}

enum class AdminTab(val label: String) {
    PRODUCTS("Products"),
    ORDERS("Orders"),
    ANALYTICS("Analytics")
}
