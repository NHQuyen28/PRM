package com.example.prm.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.prm.data.remote.dto.VoucherResp
import com.example.prm.ui.theme.PurpleJobsly
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AdminVoucherManagementScreen(
    navController: NavHostController,
    viewModel: AdminVoucherViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    // --- THÊM ĐOẠN NÀY ĐỂ TỰ ĐỘNG TẢI LẠI VOUCHER KHI QUAY LẠI MÀN HÌNH ---
    val lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                viewModel.loadVouchers() // Tải lại danh sách
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    // ----------------------------------------------------------------------
    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = { navController.navigate("admin_add_voucher") },
            modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Voucher", color = Color.White)
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PurpleJobsly)
            }
        } else if (uiState.errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = uiState.errorMessage ?: "Lỗi", color = Color.Red)
            }
        } else if (uiState.vouchers.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No vouchers found", fontSize = 16.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.vouchers) { voucher ->
                    AdminVoucherCard(
                        voucher = voucher,
                        onEdit = { navController.navigate("admin_edit_voucher/${voucher.id}") },
                        onDelete = { viewModel.deleteVoucher(voucher.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminVoucherCard(
    voucher: VoucherResp,
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = voucher.code,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = PurpleJobsly
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (voucher.isActive) "Active" else "Inactive",
                        fontSize = 10.sp,
                        color = Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (voucher.isActive) Color(0xFF4CAF50) else Color.Red)
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = voucher.name,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Giảm: " + if (voucher.discountPercentage > 0) "${voucher.discountPercentage}%" else "₫${voucher.discountAmount}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "HSD: ${formatDate(voucher.endDate)}",
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

private fun formatDate(dateString: String): String {
    return try {
        // Parse the ISO date string from .NET backend
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = parser.parse(dateString)
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        date?.let { formatter.format(it) } ?: dateString
    } catch (e: Exception) {
        dateString
    }
}