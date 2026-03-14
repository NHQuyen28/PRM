package com.example.prm.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.prm.data.remote.dto.ProfileResponse
import com.example.prm.ui.theme.PurpleJobsly
import com.example.prm.data.session.SessionManager
import com.example.prm.utils.ImageUtil
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import com.example.prm.data.remote.dto.AddressResponse
import com.example.prm.data.remote.dto.OrderResponse

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = viewModel(),
    sessionManager: SessionManager? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    // Use the provided sessionManager or create a new one if not provided
    val actualSessionManager = sessionManager ?: SessionManager(context)

    var avatarBase64 by remember { mutableStateOf("") }
    var avatarPreview by remember { mutableStateOf<Uri?>(null) }

    var showEditDialog by remember { mutableStateOf(false) }

    var showAddressDialog by remember { mutableStateOf(false) }

    var showAddresses by remember { mutableStateOf(false) }

    var showOrders by remember { mutableStateOf(false) }

    var showEditAddressDialog by remember { mutableStateOf(false) }
    var editingAddress by remember { mutableStateOf<AddressResponse?>(null) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            avatarPreview = it
            avatarBase64 = ImageUtil.uriToBase64(context, it)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            // Header
            item {
                ProfileHeaderModern(navController = navController)
            }

            when {
                uiState.isLoading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = PurpleJobsly)
                        }
                    }
                }

                uiState.profile != null -> {
                    val profile = uiState.profile!!

                    item {
                        ProfileAvatarModern(
                            avatarUrl = profile.avatarUrl,
                            avatarPreview = avatarPreview
                        )
                    }

                    item {
                        ProfileInfoCardsModern(profile)
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {

                        Button(
                            onClick = {
                                showEditDialog = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Update Profile")
                        }
                    }

                    item {
                        ProfileMenuItemModern(
                            title = "📍 Add Addresses",
                            subtitle = "Manage delivery addresses",
                            onClick = {
                                showAddressDialog = true
                            }
                        )
                    }

                    item {
                        ProfileMenuItemModern(
                            title = "📍View My Addresses",
                            subtitle = "Manage delivery addresses",
                            onClick = {
                                showAddresses = !showAddresses

                                if (showAddresses) {
                                    viewModel.loadAddresses()
                                }
                            }
                        )

                    }

                    item {
                        ProfileMenuItemModern(
                            title = "📦 My Orders",
                            subtitle = "View your order history",
                            onClick = {

                                showOrders = !showOrders

                                if (showOrders) {
                                    viewModel.loadOrders()
                                }

                            }
                        )
                    }

                    if (showOrders) {

                        item {

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                shape = RoundedCornerShape(16.dp),
                                shadowElevation = 4.dp,
                                color = Color.White
                            ) {

                                Column(modifier = Modifier.padding(16.dp)) {

                                    Text(
                                        text = "My Orders",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    if (uiState.orders.isEmpty()) {

                                        Text(
                                            "No orders yet",
                                            color = Color.Gray
                                        )

                                    } else {

                                        uiState.orders.forEach { order ->

                                            OrderItem(order)

                                            Spacer(modifier = Modifier.height(10.dp))

                                        }

                                    }

                                }

                            }

                        }

                    }



                    if (showAddresses) {

                        item {

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                shape = RoundedCornerShape(16.dp),
                                shadowElevation = 4.dp,
                                color = Color.White
                            ) {

                                Column(modifier = Modifier.padding(16.dp)) {

                                    Text(
                                        text = "Saved Addresses",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    uiState.addresses.forEach { address ->

                                        AddressItem(
                                            address = address,

                                            onSetDefault = { id ->
                                                viewModel.setDefaultAddress(id)
                                            },

                                            onDelete = { id ->
                                                viewModel.deleteAddress(id)
                                            },

                                            onEdit = { address ->
                                                editingAddress = address
                                                showEditAddressDialog = true
                                            }
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))
                                    }

                                }

                            }

                        }

                    }

                    item {
                        LogoutButtonModern(
                            sessionManager = actualSessionManager,
                            navController = navController
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }

                !uiState.error.isNullOrEmpty() -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Spacer(modifier = Modifier.height(66.dp))

                                Text(
                                    "Please log in to view your profile",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                Spacer(modifier = Modifier.height(36.dp))

                                Button(
                                    onClick = { navController.navigate("login") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Sign In", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showEditDialog && uiState.profile != null) {

            val profile = uiState.profile!!

            var fullName by remember { mutableStateOf(profile.fullName) }
            var phone by remember { mutableStateOf(profile.phone ?: "") }
            var address by remember { mutableStateOf(profile.address ?: "") }

            AlertDialog(

                onDismissRequest = { showEditDialog = false },

                title = {
                    Text("Update Profile")
                },

                text = {

                    Column {

                        Button(
                            onClick = {
                                imagePicker.launch("image/*")
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Choose Avatar")
                        }

                        avatarPreview?.let {
                            AsyncImage(
                                model = it,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .align(Alignment.CenterHorizontally),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Full Name") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Address") },
                            modifier = Modifier.fillMaxWidth()
                        )

                    }

                },

                confirmButton = {

                    Button(
                        onClick = {

                            viewModel.updateProfile(
                                fullName = fullName,
                                phone = phone,
                                address = address,
                                avatarUrl = if (avatarBase64.isNotEmpty())
                                    avatarBase64
                                else
                                    profile.avatarUrl ?: ""
                            )

                            showEditDialog = false
                        },colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Save")
                    }

                },

                dismissButton = {

                    TextButton(
                        onClick = { showEditDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE53935),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cancel")
                    }

                }

            )
        }

        if (showEditAddressDialog && editingAddress != null) {

            var recipientName by remember { mutableStateOf(editingAddress!!.recipientName) }
            var phone by remember { mutableStateOf(editingAddress!!.phone) }
            var province by remember { mutableStateOf(editingAddress!!.province) }
            var district by remember { mutableStateOf(editingAddress!!.district) }
            var ward by remember { mutableStateOf(editingAddress!!.ward) }
            var detailAddress by remember { mutableStateOf(editingAddress!!.detailAddress) }
            var isDefault by remember { mutableStateOf(editingAddress!!.isDefault) }

            AlertDialog(

                onDismissRequest = { showEditAddressDialog = false },

                title = { Text("Edit Address") },

                text = {

                    Column {

                        OutlinedTextField(
                            value = recipientName,
                            onValueChange = { recipientName = it },
                            label = { Text("Recipient Name") }
                        )

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone") }
                        )

                        OutlinedTextField(
                            value = province,
                            onValueChange = { province = it },
                            label = { Text("Province") }
                        )

                        OutlinedTextField(
                            value = district,
                            onValueChange = { district = it },
                            label = { Text("District") }
                        )

                        OutlinedTextField(
                            value = ward,
                            onValueChange = { ward = it },
                            label = { Text("Ward") }
                        )

                        OutlinedTextField(
                            value = detailAddress,
                            onValueChange = { detailAddress = it },
                            label = { Text("Detail Address") }
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Checkbox(
                                checked = isDefault,
                                onCheckedChange = { isDefault = it }
                            )

                            Text("Set as default")
                        }
                    }
                },

                confirmButton = {

                    Button(
                        onClick = {

                            viewModel.updateAddress(
                                editingAddress!!.id,
                                recipientName,
                                phone,
                                province,
                                district,
                                ward,
                                detailAddress,
                                isDefault
                            )

                            showEditAddressDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Save")
                    }

                },

                dismissButton = {

                    Button(
                        onClick = { showEditAddressDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE53935),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cancel")
                    }

                }
            )
        }

        if (showAddressDialog) {

            var recipientName by remember { mutableStateOf("") }
            var phone by remember { mutableStateOf("") }
            var province by remember { mutableStateOf("") }
            var district by remember { mutableStateOf("") }
            var ward by remember { mutableStateOf("") }
            var detailAddress by remember { mutableStateOf("") }
            var isDefault by remember { mutableStateOf(false) }

            AlertDialog(

                onDismissRequest = { showAddressDialog = false },

                title = { Text("Add New Address") },

                text = {
                    Column {

                        OutlinedTextField(
                            value = recipientName,
                            onValueChange = { recipientName = it },
                            label = { Text("Recipient Name") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = province,
                            onValueChange = { province = it },
                            label = { Text("Province") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = district,
                            onValueChange = { district = it },
                            label = { Text("District") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = ward,
                            onValueChange = { ward = it },
                            label = { Text("Ward") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = detailAddress,
                            onValueChange = { detailAddress = it },
                            label = { Text("Detail Address") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Checkbox(
                                checked = isDefault,
                                onCheckedChange = { isDefault = it }
                            )

                            Text("Set as default address")
                        }
                    }
                },

                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.createAddress(
                                recipientName,
                                phone,
                                province,
                                district,
                                ward,
                                detailAddress,
                                isDefault
                            )
                            showAddressDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Save")
                    }
                },

                dismissButton = {
                    Button(
                        onClick = { showAddressDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE53935),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun AddressItem(
    address: AddressResponse,
    onSetDefault: (String) -> Unit,
    onDelete: (String) -> Unit,
    onEdit: (AddressResponse) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8F8F8), RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = address.recipientName,
                fontWeight = FontWeight.Bold
            )

            if (address.isDefault) {

                Text(
                    text = "Default",
                    color = Color.White,
                    modifier = Modifier
                        .background(Color(0xFF27AE60), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(address.phone)

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = address.fullAddress,
            color = Color.Gray,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

            Button(
                onClick = { onSetDefault(address.id) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF27AE60)
                )
            ) {
                Text("Set Default")
            }

            Button(
                onClick = { onEdit(address) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF39C12)
                )
            ) {
                Text("Edit")
            }

            Button(
                onClick = { onDelete(address.id) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE74C3C)
                )
            ) {
                Text("Delete")
            }
        }
    }
}

@Composable
private fun ProfileHeaderModern(navController: NavHostController) {
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
            verticalAlignment = Alignment.CenterVertically
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
                Text("My Account", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                Text("Manage your profile", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun ProfileAvatarModern(
    avatarUrl: String?,
    avatarPreview: Uri?
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .shadow(8.dp, CircleShape),
            contentAlignment = Alignment.Center
        ) {

            when {

                avatarPreview != null -> {
                    AsyncImage(
                        model = avatarPreview,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                !avatarUrl.isNullOrEmpty() -> {

                    val imageBytes = Base64.decode(avatarUrl, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                else -> {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = PurpleJobsly,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoCardsModern(profile: ProfileResponse) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp)),
        color = Color.White
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            ProfileRowModern(Icons.Default.Person, "Full Name", profile.fullName, PurpleJobsly)
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFEEE))
            Spacer(modifier = Modifier.height(16.dp))

            ProfileRowModern(Icons.Default.Email, "Email", profile.email, Color(0xFF00A8FF))
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFEEE))
            Spacer(modifier = Modifier.height(16.dp))

            ProfileRowModern(Icons.Default.Phone, "Phone", profile.phone ?: "Not added", Color(0xFF27AE60))
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFEEE))
            Spacer(modifier = Modifier.height(16.dp))

            ProfileRowModern(Icons.Default.LocationOn, "Address", profile.address ?: "Not added", Color(0xFFE67E22))
        }
    }
}

@Composable
private fun ProfileRowModern(icon: ImageVector, title: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(color.copy(alpha = 0.15f), shape = RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 11.sp, color = Color(0xFF999), fontWeight = FontWeight.SemiBold)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun ProfileMenuItemModern(title: String, subtitle: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp)),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(subtitle, fontSize = 11.sp, color = Color(0xFF999), modifier = Modifier.padding(top = 4.dp))
            }
            Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color(0xFFCCC))
        }
    }
}

@Composable
private fun LogoutButtonModern(sessionManager: SessionManager, navController: NavHostController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = {
                sessionManager.logout()
                navController.navigate("login") { popUpTo(0); launchSingleTop = true }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
fun OrderItem(order: OrderResponse) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8F8F8), RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {

        Text("Status: ${order.statusDisplay}")

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Total: ₫${String.format("%,.2f", order.totalAmount)}",
            color = Color(0xFF27AE60),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        order.orderDetails.forEach {

            Text(
                text = "${it.productName} x${it.quantity}",
                fontSize = 13.sp,
                color = Color.Gray
            )

        }

    }

}