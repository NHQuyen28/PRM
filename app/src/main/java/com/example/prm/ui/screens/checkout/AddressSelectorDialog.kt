package com.example.prm.ui.screens.checkout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.prm.data.remote.dto.AddressResponse
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn

@Composable
fun AddressSelectorDialog(

    addresses: List<AddressResponse>,
    onSelect: (AddressResponse) -> Unit,
    onDismiss: () -> Unit

) {

    AlertDialog(

        onDismissRequest = onDismiss,

        confirmButton = {},

        title = { Text("Select Address") },

        text = {

            LazyColumn {

                items(addresses) { address ->

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                                onSelect(address)
                                onDismiss()

                            }
                            .padding(12.dp)
                    ) {

                        Text(address.recipientName)

                        Text(address.phone)

                        Text(address.fullAddress)

                    }

                }

            }

        }

    )

}