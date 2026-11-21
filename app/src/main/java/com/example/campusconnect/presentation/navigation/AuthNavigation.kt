package com.example.campusconnect.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.campusconnect.presentation.auth.LoginScreen
import com.example.campusconnect.presentation.auth.RegisterScreen
import com.example.campusconnect.presentation.auth.viewmodel.AuthViewModel
import com.example.campusconnect.presentation.navigation.routes.LoginRoute
import com.example.campusconnect.presentation.navigation.routes.RegisterRoute

@Composable
fun AuthNavigation(
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginRoute
    ) {
        composable<LoginRoute> {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(RegisterRoute)
                },
                onGoogleSignInClick = {},
                onForgotPasswordClick = {}
            )
        }

        composable<RegisterRoute> {
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
    }
}