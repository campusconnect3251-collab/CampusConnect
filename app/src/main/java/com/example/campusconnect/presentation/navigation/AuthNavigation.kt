package com.example.campusconnect.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.campusconnect.presentation.auth.LoginScreen
import com.example.campusconnect.presentation.auth.RegisterScreen

@Composable
fun AuthNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginRoute
    ){
        composable<LoginRoute> {
            LoginScreen()
        }

        composable<RegisterRoute>{
            RegisterScreen()
        }
    }
}