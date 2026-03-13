package com.example.prm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.prm.data.remote.RetrofitClient
import com.example.prm.data.session.SessionManager
import com.example.prm.ui.screens.cart.CartScreen
import com.example.prm.ui.screens.login.LoginScreen
import com.example.prm.ui.screens.register.RegisterScreen
import com.example.prm.ui.screens.home.HomeScreen
import com.example.prm.ui.screens.products.ProductListScreen
import com.example.prm.ui.screens.product_detail.ProductDetailScreen
import com.example.prm.ui.screens.admin.AdminDashboardScreen
import com.example.prm.ui.screens.admin.AdminAddProductScreen
import com.example.prm.ui.screens.admin.AdminEditProductScreen
import com.example.prm.ui.screens.checkout.CheckoutScreen
import com.example.prm.ui.screens.profile.ProfileScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    // Check if user is already logged in
    val context = LocalContext.current
    val sessionManager = SessionManager(context)

    // Initialize Retrofit with the current session so that all API calls use the token
    RetrofitClient.initSessionManager(sessionManager)

    val startDestination = if (sessionManager.isLoggedIn()) "home" else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
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
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val search = backStackEntry.arguments?.getString("search")
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            ProductListScreen(
                navController = navController,
                initialSearch = search,
                initialCategoryId = categoryId
            )
        }

        composable(
            route = "product_detail/{productId}",
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(
                productId = productId,
                navController = navController
            )
        }


        // Admin screens
        composable(route = "admin_dashboard") {
            AdminDashboardScreen(navController = navController)
        }

        composable(route = "admin_add_product") {
            AdminAddProductScreen(navController = navController)
        }

        composable(
            route = "admin_edit_product/{productId}",
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            AdminEditProductScreen(
                productId = productId,
                navController = navController
            )
        }
        composable("profile") {
            ProfileScreen(navController)
        }

        composable("cart") {
            CartScreen(navController)
        }

        composable("checkout") {
            CheckoutScreen(navController)
        }

    }
}