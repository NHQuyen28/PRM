package com.example.prm.ui.screens.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.prm.data.remote.dto.ProductVariantResp
import com.example.prm.ui.theme.PurpleJobsly

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEditProductScreen(
    navController: NavHostController,
    productId: String,
    viewModel: AdminEditProductViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // States for Product
    var productName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var basePrice by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("") }
    var brandId by remember { mutableStateOf("") }
    var slug by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    // States for Dialogs
    var showAddVariantDialog by remember { mutableStateOf(false) }
    var variantToEdit by remember { mutableStateOf<ProductVariantResp?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadProduct(productId)
        viewModel.loadCategories()
        viewModel.loadBrands()
    }

    LaunchedEffect(uiState.product) {
        uiState.product?.let { product ->
            productName = product.productName
            description = product.description ?: ""
            basePrice = product.basePrice.toString()
            categoryId = product.categoryId
            brandId = product.brandId
            slug = product.slug ?: ""
            imageUrl = product.images?.firstOrNull()?.imageUrl ?: ""
        }
    }

    // Handle Toasts
    LaunchedEffect(uiState.successMessage, uiState.errorMessage) {
        uiState.successMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessages()
        }
        uiState.errorMessage?.let {
            Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
            viewModel.clearMessages()
        }
    }

    LaunchedEffect(uiState.isProductUpdateSuccess) {
        if (uiState.isProductUpdateSuccess) {
            Toast.makeText(context, "Product updated successfully!", Toast.LENGTH_SHORT).show()
            navController.navigateUp()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFFAFAFA))) {
        TopAppBar(
            title = { Text("Edit Product") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = PurpleJobsly,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            )
        )

        if (uiState.isLoading && uiState.product == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PurpleJobsly)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ==========================================
                // 1. THÔNG TIN SẢN PHẨM CHÍNH
                // ==========================================
                item { Text("General Information", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PurpleJobsly) }

                item {
                    OutlinedTextField(
                        value = productName, onValueChange = { productName = it },
                        label = { Text("Product Name *") }, modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
                item {
                    OutlinedTextField(
                        value = imageUrl, onValueChange = { imageUrl = it },
                        label = { Text("Image URL") }, modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = basePrice, onValueChange = { basePrice = it },
                            label = { Text("Base Price (₫) *") }, modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = slug, onValueChange = { slug = it },
                            label = { Text("Slug") }, modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(modifier = Modifier.weight(1f)) {
                            EditCategorySelectField(uiState.categories, categoryId) { categoryId = it }
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            EditBrandSelectField(uiState.brands, brandId) { brandId = it }
                        }
                    }
                }
                item {
                    OutlinedTextField(
                        value = description, onValueChange = { description = it },
                        label = { Text("Description") }, modifier = Modifier.fillMaxWidth().height(100.dp),
                        maxLines = 5, shape = RoundedCornerShape(8.dp)
                    )
                }
                item {
                    Button(
                        onClick = {
                            viewModel.updateProduct(productId, productName, description, basePrice.toDoubleOrNull() ?: 0.0, categoryId, brandId, slug, imageUrl)
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly),
                        enabled = !uiState.isLoading
                    ) {
                        Text("Save Main Product Info", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                item { Divider(modifier = Modifier.padding(vertical = 8.dp)) }

                // ==========================================
                // 2. QUẢN LÝ BIẾN THỂ (PRODUCT VARIANTS)
                // ==========================================
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Product Variants", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PurpleJobsly)
                        TextButton(onClick = { showAddVariantDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Option", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                if (uiState.product?.variants.isNullOrEmpty()) {
                    item { Text("No variants yet. Product will use Base Price.", color = Color.Gray, fontSize = 14.sp) }
                } else {
                    items(uiState.product!!.variants!!) { variant ->
                        VariantCard(variant) { variantToEdit = variant }
                    }
                }
                item { Spacer(modifier = Modifier.height(30.dp)) }
            }
        }
    }

    // ==========================================
    // DIALOGS
    // ==========================================
    if (showAddVariantDialog) {
        VariantDialog(
            isEdit = false,
            variant = null,
            isLoading = uiState.isVariantLoading,
            onDismiss = { showAddVariantDialog = false },
            onSave = { sku, size, color, weight, grip, tension, stock, priceAdj, _ ->
                viewModel.addVariant(productId, sku, size, color, weight, grip, tension, stock, priceAdj)
                showAddVariantDialog = false
            }
        )
    }

    if (variantToEdit != null) {
        VariantDialog(
            isEdit = true,
            variant = variantToEdit,
            isLoading = uiState.isVariantLoading,
            onDismiss = { variantToEdit = null },
            onSave = { _, size, color, _, _, _, stock, priceAdj, isActive ->
                viewModel.updateVariant(productId, variantToEdit!!.id, size, color, stock, priceAdj, isActive)
                variantToEdit = null
            }
        )
    }
}

// Thẻ hiển thị Biến thể
@Composable
private fun VariantCard(variant: ProductVariantResp, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 2.dp,
        color = Color.White,
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(variant.sku, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                val props = listOfNotNull(variant.color, variant.size, variant.weight).filter { it.isNotBlank() }
                Text(if (props.isNotEmpty()) props.joinToString(" | ") else "Default Option", fontSize = 12.sp, color = Color.DarkGray)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Stock: ${variant.stockQuantity}", fontSize = 12.sp, color = if (variant.stockQuantity > 0) Color(0xFF388E3C) else Color.Red)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Price Adj: ₫${variant.priceAdjustment}", fontSize = 12.sp, color = Color.Gray)
                }
            }
            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = PurpleJobsly)
        }
    }
}

// Dialog dùng chung cho Thêm & Sửa Variant
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VariantDialog(
    isEdit: Boolean,
    variant: ProductVariantResp?,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onSave: (sku: String, size: String, color: String, weight: String, grip: String, tension: String, stock: Int, priceAdj: Double, isActive: Boolean) -> Unit
) {
    var sku by remember { mutableStateOf(variant?.sku ?: "") }
    var size by remember { mutableStateOf(variant?.size ?: "") }
    var color by remember { mutableStateOf(variant?.color ?: "") }
    var weight by remember { mutableStateOf(variant?.weight ?: "") }
    var stock by remember { mutableStateOf(variant?.stockQuantity?.toString() ?: "10") }
    var priceAdj by remember { mutableStateOf(variant?.priceAdjustment?.toString() ?: "0") }
    var isActive by remember { mutableStateOf(variant?.isActive ?: true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEdit) "Edit Variant" else "Add New Variant", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (!isEdit) {
                    OutlinedTextField(value = sku, onValueChange = { sku = it }, label = { Text("SKU (Unique) *") }, singleLine = true)
                } else {
                    Text("SKU: $sku", fontWeight = FontWeight.Bold, color = Color.Gray) // SKU không được sửa
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = size, onValueChange = { size = it }, label = { Text("Size") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = color, onValueChange = { color = it }, label = { Text("Color") }, modifier = Modifier.weight(1f), singleLine = true)
                }

                if (!isEdit) {
                    if (!isEdit) {
                        OutlinedTextField(
                            value = weight,
                            onValueChange = { weight = it },
                            label = { Text("Weight (e.g. 3U, 4U)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock *") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = priceAdj, onValueChange = { priceAdj = it }, label = { Text("Price Adj (₫)") }, modifier = Modifier.weight(1f), singleLine = true)
                }

                if (isEdit) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isActive, onCheckedChange = { isActive = it })
                        Text("Active (Visible to users)")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(sku, size, color, weight, "", "", stock.toIntOrNull() ?: 0, priceAdj.toDoubleOrNull() ?: 0.0, isActive) },
                enabled = !isLoading && (sku.isNotBlank() || isEdit)
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = Color.Gray) }
        }
    )
}

// ==========================================
// DROPDOWNS GIỮ NGUYÊN (Để tránh lỗi code)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditCategorySelectField(categories: List<CategoryItem>, selectedId: String, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = categories.find { it.id == selectedId }?.name ?: "Select Category", onValueChange = {},
            readOnly = true, modifier = Modifier.fillMaxWidth().menuAnchor(), trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            categories.forEach { DropdownMenuItem(text = { Text(it.name) }, onClick = { onSelect(it.id); expanded = false }) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditBrandSelectField(brands: List<BrandItem>, selectedId: String, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = brands.find { it.id == selectedId }?.name ?: "Select Brand", onValueChange = {},
            readOnly = true, modifier = Modifier.fillMaxWidth().menuAnchor(), trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            brands.forEach { DropdownMenuItem(text = { Text(it.name) }, onClick = { onSelect(it.id); expanded = false }) }
        }
    }
}