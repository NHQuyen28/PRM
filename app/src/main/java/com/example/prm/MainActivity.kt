package com.example.prm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
// Nhớ import PRMTheme từ gói ui.theme của bạn
import com.example.prm.ui.theme.PRMTheme
import com.example.prm.ui.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Thay đổi từ MaterialTheme sang PRMTheme
            PRMTheme {
                AppNavigation()
            }
        }
    }
}