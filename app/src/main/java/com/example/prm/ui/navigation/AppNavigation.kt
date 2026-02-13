package com.example.prm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.prm.ui.screens.login.LoginScreen
import com.example.prm.ui.screens.register.RegisterScreen
import com.example.prm.ui.screens.home.HomeScreen
import com.example.prm.ui.screens.products.ProductListScreen
import com.example.prm.ui.screens.product_detail.ProductDetailScreen
import com.example.prm.ui.screens.cart.CartScreen
import com.example.prm.ui.screens.checkout.CheckoutScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // Authentication screens
        composable(
            route = "login?autoExpand={autoExpand}",
            arguments = listOf(
                navArgument("autoExpand") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val autoExpand = backStackEntry.arguments?.getBoolean("autoExpand") ?: false
            LoginScreen(
                navController = navController,
                autoExpand = autoExpand
            )
        }

        composable(
            route = "register?autoExpand={autoExpand}",
            arguments = listOf(
                navArgument("autoExpand") {
                    type = NavType.BoolType
                    defaultValue = true
                }
            )
        ) { backStackEntry ->
            val autoExpand = backStackEntry.arguments?.getBoolean("autoExpand") ?: true
            RegisterScreen(
                navController = navController,
                autoExpand = autoExpand
            )
        }

        // Main app screens
        composable(route = "home") {
            HomeScreen(navController = navController)
        }

        composable(
            route = "products?search={search}&categoryId={categoryId}",
            arguments = listOf(
                navArgument("search") {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                },
                navArgument("categoryId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val search = backStackEntry.arguments?.getString("search")
            val categoryId = backStackEntry.arguments?.getInt("categoryId")
            ProductListScreen(
                navController = navController,
                initialSearch = search,
                initialCategoryId = if (categoryId == -1) null else categoryId
            )
        }

        composable(
            route = "product_detail/{productId}",
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            ProductDetailScreen(
                productId = productId,
                navController = navController
            )
        }

        composable(route = "cart") {
            CartScreen(navController = navController)
        }

        composable(route = "checkout") {
            CheckoutScreen(navController = navController)
        }
    }
}