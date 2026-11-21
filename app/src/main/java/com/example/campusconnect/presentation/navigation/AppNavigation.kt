package com.example.campusconnect.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.campusconnect.presentation.auth.state.AuthState
import com.example.campusconnect.presentation.auth.viewmodel.AuthViewModel
import com.example.campusconnect.presentation.splash.SplashScreen

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    when (authState) {

        AuthState.Loading -> {
            SplashScreen()
        }

        is AuthState.Authenticated -> {
            MainNavigation()
        }

        AuthState.Unauthenticated -> {
            AuthNavigation(
                authViewModel = authViewModel
            )
        }
    }
}