package com.example.prm.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.prm.data.remote.dto.ProfileResponse
import com.example.prm.ui.theme.PurpleJobsly
import com.example.prm.utils.ImageUtil
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.Image
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import com.example.prm.data.session.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val sessionManager = remember { SessionManager(context) }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {

        TopAppBar(
            title = { Text("Profile", color = Color.White) },

            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                }
            },

            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = PurpleJobsly
            )
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {

            when {

                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.profile != null -> {

                    val profile = uiState.profile!!

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Avatar(profile.avatarUrl)

                        Spacer(modifier = Modifier.height(24.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {

                            Column(
                                modifier = Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {

                                ProfileItem("Name", profile.fullName)
                                ProfileItem("Email", profile.email)
                                ProfileItem("Phone", profile.phone ?: "N/A")
                                ProfileItem("Address", profile.address ?: "N/A")

                                Spacer(modifier = Modifier.height(10.dp))

                                Button(
                                    onClick = { showEditDialog = true },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Update Profile")
                                }
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        TextButton(
                            onClick = {
                                sessionManager.logout()

                                navController.navigate("login") {
                                    popUpTo(0)
                                    launchSingleTop = true
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Logout",
                                tint = Color.Red
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text("Logout", color = Color.Red)
                        }
                    }

                    if (showEditDialog) {

                        UpdateProfileDialog(
                            profile = profile,
                            onDismiss = { showEditDialog = false },
                            onSave = { name, phone, address, avatar ->

                                viewModel.updateProfile(
                                    name,
                                    phone,
                                    address,
                                    avatar
                                )

                                showEditDialog = false
                            }
                        )
                    }
                }

                uiState.error != null -> {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.align(Alignment.Center)
                    ) {

                        Text(uiState.error!!)

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                navController.navigate("login") {
                                    popUpTo(0)
                                }
                            }
                        ) {
                            Text("Login")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Avatar(base64: String?) {

    val bitmap = remember(base64) {
        try {
            if (!base64.isNullOrEmpty()) {
                val bytes = Base64.decode(base64, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } else null
        } catch (e: Exception) {
            null
        }
    }

    Box(
        modifier = Modifier
            .size(130.dp)
            .clip(CircleShape)
            .background(Color.White)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {

        if (bitmap != null) {

            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Avatar",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

        } else {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(PurpleJobsly.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    Icons.Default.Person,
                    contentDescription = "Avatar",
                    modifier = Modifier.size(60.dp),
                    tint = PurpleJobsly
                )
            }
        }
    }
}

@Composable
fun ProfileItem(title: String, value: String) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = title,
            color = Color.Gray
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun UpdateProfileDialog(
    profile: ProfileResponse,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit
) {

    var name by remember { mutableStateOf(profile.fullName) }
    var phone by remember { mutableStateOf(profile.phone ?: "") }
    var address by remember { mutableStateOf(profile.address ?: "") }
    var avatar by remember { mutableStateOf(profile.avatarUrl ?: "") }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->

        if (uri != null) {

            avatar = ImageUtil.uriToBase64(context, uri)

        }
    }

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {
            Text("Update Profile")
        },

        confirmButton = {

            Button(
                onClick = {
                    onSave(name, phone, address, avatar)
                }
            ) {
                Text("Save")
            }
        },

        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },

        text = {

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Avatar(avatar)

                Button(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Choose Avatar")
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") }
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") }
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") }
                )
            }
        }
    )
}