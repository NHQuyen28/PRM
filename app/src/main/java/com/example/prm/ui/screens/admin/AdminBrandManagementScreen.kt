package com.example.prm.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.example.prm.data.remote.dto.BrandApi as BrandApiDto
import com.example.prm.ui.theme.PurpleJobsly

@Composable
fun AdminBrandManagementScreen(
    viewModel: AdminBrandManagementViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var brandName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val editingBrand = uiState.editingBrand

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "Manage Brands",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Add Button
        Button(
            onClick = {
                brandName = ""
                description = ""
                viewModel.setEditingBrand(null)
                showDialog = true
            },
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly)
        ) {
            Text("+ Add Brand", color = Color.White)
        }

        // Messages
        if (uiState.successMessage != null) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .padding(bottom = 8.dp),
                color = Color(0xFFE8F5E9)
            ) {
                Text(
                    uiState.successMessage ?: "",
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.padding(12.dp),
                    fontSize = 12.sp
                )
            }
        }

        if (uiState.errorMessage != null) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .padding(bottom = 8.dp),
                color = Color(0xFFFFEBEE)
            ) {
                Text(
                    uiState.errorMessage ?: "",
                    color = Color(0xFFC62828),
                    modifier = Modifier.padding(12.dp),
                    fontSize = 12.sp
                )
            }
        }

        // Brands List
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PurpleJobsly)
            }
        } else if (uiState.brands.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No brands found", fontSize = 14.sp)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(uiState.brands) { brand ->
                    BrandCard(
                        brand = brand,
                        onEdit = {
                            brandName = brand.name
                            description = ""
                            viewModel.setEditingBrand(brand)
                            showDialog = true
                        },
                        onDelete = {
                            viewModel.deleteBrand(brand.id)
                        }
                    )
                }
            }
        }
    }

    // Add/Edit Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    if (editingBrand != null) "Edit Brand" else "Add Brand",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = brandName,
                        onValueChange = { brandName = it },
                        label = { Text("Brand Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF5F5F5)
                        )
                    )
                    TextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description (Optional)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF5F5F5)
                        ),
                        maxLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editingBrand != null) {
                            viewModel.updateBrand(
                                editingBrand.id,
                                brandName,
                                description.ifBlank { null }
                            )
                        } else {
                            viewModel.createBrand(
                                brandName,
                                description.ifBlank { null }
                            )
                        }
                        showDialog = false
                        viewModel.clearMessages()
                    },
                    enabled = !uiState.isSubmitting,
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly)
                ) {
                    Text(if (editingBrand != null) "Update" else "Create", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                ) {
                    Text("Cancel", color = Color.Black)
                }
            }
        )
    }
}

@Composable
private fun BrandCard(
    brand: BrandApiDto,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        color = Color(0xFFF9F9F9),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    brand.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    "ID: ${brand.id.take(8)}...",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
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
