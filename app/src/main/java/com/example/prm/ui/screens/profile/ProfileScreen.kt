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

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    var avatarBase64 by remember { mutableStateOf("") }
    var avatarPreview by remember { mutableStateOf<Uri?>(null) }

    var showEditDialog by remember { mutableStateOf(false) }

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
                        ProfileInfoCardsModern(profile)
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        ProfileMenuItemModern(
                            title = "?? Order History",
                            subtitle = "View your orders",
                            onClick = { navController.navigate("orders") }
                        )
                    }

                    item {
                        ProfileMenuItemModern(
                            title = "?? Settings",
                            subtitle = "Account preferences",
                            onClick = { }
                        )
                    }

                    item {
                        ProfileMenuItemModern(
                            title = "? Help",
                            subtitle = "FAQs and support",
                            onClick = { }
                        )
                    }

                    item {
                        LogoutButtonModern(
                            sessionManager = sessionManager,
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
                                Text(uiState.error ?: "Error", color = Color.Red)
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { navController.navigate("login") { popUpTo(0) } },
                                    colors = ButtonDefaults.buttonColors(containerColor = PurpleJobsly)
                                ) {
                                    Text("Login Again")
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
                        }
                    ) {
                        Text("Save")
                    }

                },

                dismissButton = {

                    TextButton(
                        onClick = { showEditDialog = false }
                    ) {
                        Text("Cancel")
                    }

                }

            )
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
