package com.example.prm.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prm.data.remote.RetrofitClient
import com.example.prm.data.repository.AuthRepository
import com.example.prm.data.session.SessionManager
import com.example.prm.ui.login.LoginScreen
import com.example.prm.ui.login.LoginViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val authRepository = remember {
        AuthRepository(RetrofitClient.api)
    }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // 🔐 LOGIN
        composable("login") {
            val vm = remember {
                LoginViewModel(
                    authRepository,
                    SessionManager(context)
                )
            }

            LoginScreen(
                navController = navController,
                viewModel = vm
            )
        }


    }
}
