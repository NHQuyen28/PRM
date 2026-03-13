package com.example.prm.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
fun AdminEditVoucherScreen(
    voucherId: String,
    navController: NavHostController,
    viewModel: AdminEditVoucherViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var code by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var discountAmount by remember { mutableStateOf("0") }
    var discountPercentage by remember { mutableStateOf("0") }
    var minOrder by remember { mutableStateOf("0") }
    var maxDiscount by remember { mutableStateOf("0") }
    var usageLimit by remember { mutableStateOf("100") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(true) }

    // Gọi API tải dữ liệu Voucher khi mở màn hình
    LaunchedEffect(voucherId) {
        viewModel.loadVoucher(voucherId)
    }

    // Khi tải xong, điền dữ liệu vào các ô
    LaunchedEffect(uiState.voucher) {
        uiState.voucher?.let { v ->
            code = v.code
            name = v.name
            description = v.description ?: ""
            discountAmount = v.discountAmount.toString()
            discountPercentage = v.discountPercentage.toString()
            minOrder = v.minimumOrderAmount.toString()
            maxDiscount = v.maximumDiscountAmount.toString()
            usageLimit = v.usageLimit.toString()
            isActive = v.isActive

            // Cắt chuỗi ngày tháng của BE (VD: 2026-03-13T08:27:30Z) thành YYYY-MM-DD cho dễ nhìn
            startDate = if (v.startDate.length >= 10) v.startDate.substring(0, 10) else v.startDate
            endDate = if (v.endDate.length >= 10) v.endDate.substring(0, 10) else v.endDate
        }
    }

    // Lắng nghe sự kiện lưu thành công để quay về
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.navigateUp()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        TopAppBar(
            title = { Text("Edit Voucher") },
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

        if (uiState.isLoading && uiState.voucher == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PurpleJobsly)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Text("Voucher Information", fontWeight = FontWeight.Bold, fontSize = 16.sp) }

                item {
                    OutlinedTextField(
                        value = code, onValueChange = { code = it.uppercase() },
                        label = { Text("Mã Voucher (Code) *") }, modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = name, onValueChange = { name = it },
                        label = { Text("Tên chiến dịch *") }, modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = description, onValueChange = { description = it },
                        label = { Text("Mô tả chi tiết") }, modifier = Modifier.fillMaxWidth()
                    )
                }

                item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }
                item { Text("Cấu hình Giảm giá & Điều kiện", fontWeight = FontWeight.Bold) }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = discountPercentage, onValueChange = { discountPercentage = it },
                            label = { Text("Giảm %") }, modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = discountAmount, onValueChange = { discountAmount = it },
                            label = { Text("Giảm Tiền mặt") }, modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = minOrder, onValueChange = { minOrder = it },
                            label = { Text("Đơn tối thiểu") }, modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = maxDiscount, onValueChange = { maxDiscount = it },
                            label = { Text("Giảm tối đa") }, modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    OutlinedTextField(
                        value = usageLimit, onValueChange = { usageLimit = it },
                        label = { Text("Số lượt dùng tối đa") }, modifier = Modifier.fillMaxWidth()
                    )
                }

                item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }
                item { Text("Thời gian áp dụng (YYYY-MM-DD)", fontWeight = FontWeight.Bold) }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = startDate, onValueChange = { startDate = it },
                            label = { Text("Ngày bắt đầu") }, modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = endDate, onValueChange = { endDate = it },
                            label = { Text("Ngày kết thúc") }, modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isActive,
                            onCheckedChange = { isActive = it },
                            colors = CheckboxDefaults.colors(checkedColor = PurpleJobsly)
                        )
                        Text("Voucher đang hoạt động (Active)")
                    }
                }

                item {
                    if (uiState.errorMessage != null) {
                        Text(text = "Lỗi: ${uiState.errorMessage}", color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                }

                item {
                    Button(
                        onClick = {
                            viewModel.updateVoucher(
                                id = voucherId, code, name, description,
                                discountAmount.toDoubleOrNull() ?: 0.0,
                                discountPercentage.toDoubleOrNull() ?: 0.0,
                                minOrder.toDoubleOrNull() ?: 0.0,
                                maxDiscount.toDoubleOrNull() ?: 0.0,
                                usageLimit.toIntOrNull() ?: 1,
                                startDate, endDate, isActive
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly),
                        enabled = !uiState.isLoading
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Lưu Thay Đổi", color = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}