package com.example.prm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.prm.ui.screens.login.LoginScreen
import com.example.prm.ui.screens.register.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // ✅ Login với tham số autoExpand
        composable(
            route = "login?autoExpand={autoExpand}",
            arguments = listOf(
                navArgument("autoExpand") {
                    type = NavType.BoolType
                    defaultValue = false  // Mặc định không tự mở
                }
            )
        ) { backStackEntry ->
            val autoExpand = backStackEntry.arguments?.getBoolean("autoExpand") ?: false
            LoginScreen(
                navController = navController,
                autoExpand = autoExpand
            )
        }

        // ✅ Register với tham số autoExpand
        composable(
            route = "register?autoExpand={autoExpand}",
            arguments = listOf(
                navArgument("autoExpand") {
                    type = NavType.BoolType
                    defaultValue = true  // Mặc định tự mở
                }
            )
        ) { backStackEntry ->
            val autoExpand = backStackEntry.arguments?.getBoolean("autoExpand") ?: true
            RegisterScreen(
                navController = navController,
                autoExpand = autoExpand
            )
        }
    }
}