package com.example.prm.ui.screens.cart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.example.prm.data.remote.dto.VoucherResp


@Composable
fun VoucherDialog(
    vouchers: List<VoucherResp>,
    onSelect: (VoucherResp) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text("Chọn Voucher") },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            ) {
                items(vouchers) { voucher ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelect(voucher)
                                onDismiss()
                            }
                            .padding(12.dp)
                    ) {
                        Text(
                            text = voucher.name,
                            fontWeight = FontWeight.Bold
                        )

                        Text("Code: ${voucher.code}")

                        Text(
                            text = "Giảm: ${
                                voucher.discountAmount?.toInt() ?: 0
                            }đ / ${voucher.discountPercentage ?: 0}%"
                        )

                        Divider(modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
        }
    )
}