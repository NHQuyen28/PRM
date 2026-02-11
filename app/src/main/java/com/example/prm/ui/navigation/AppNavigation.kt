package com.example.prm.ui.navigation // Phải khớp với thư mục ui/navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// Đường dẫn này phải đi qua 'screens' để tìm thấy LoginScreen
import com.example.prm.ui.screens.login.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController)
        }
    }
}
