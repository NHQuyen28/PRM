package com.example.prm.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import com.example.prm.ui.theme.PurpleJobsly

@Composable
fun MapScreen(
    onBackClick: () -> Unit,
    recipientName: String,
    phone: String,
    fullAddress: String,
    latitude: Double = 10.7769,
    longitude: Double = 106.6869
) {
    val context = LocalContext.current
    
    // Initialize osmdroid
    LaunchedEffect(Unit) {
        Configuration.getInstance().load(context, android.preference.PreferenceManager.getDefaultSharedPreferences(context))
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        // OpenStreetMap using osmdroid
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { factoryContext ->
                MapView(factoryContext).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    val controller = controller
                    controller.setZoom(15.0)
                    controller.setCenter(GeoPoint(latitude, longitude))
                }
            }
        )

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(PurpleJobsly, Color(0xFF7C3AED))
                    )
                )
                .padding(16.dp)
                .align(Alignment.TopCenter)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onBackClick() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.2f), shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Delivery Location",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Address Info Card at Bottom
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            shadowElevation = 8.dp,
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    recipientName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleJobsly
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Phone: $phone",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    fullAddress,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        }
    }
}
