package com.example.campusconnect.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.campusconnect.presentation.auth.state.AuthState
import com.example.campusconnect.presentation.auth.viewmodel.AuthViewModel
import com.example.campusconnect.presentation.profile.setup.ProfileCheckErrorScreen
import com.example.campusconnect.presentation.profile.setup.ProfileSetupScreen
import com.example.campusconnect.presentation.splash.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState
        .collectAsStateWithLifecycle()

    when (val state = authState) {

        AuthState.Loading -> {
            SplashScreen()
        }

        AuthState.Unauthenticated -> {
            AuthNavigation(
                authViewModel = authViewModel
            )
        }

        is AuthState.ProfileSetupRequired -> {
            ProfileSetupScreen(
                onProfileCreated = {}
            )
        }

        is AuthState.Authenticated -> {
            MainNavigation()
        }

        is AuthState.ProfileCheckFailed -> {
            ProfileCheckErrorScreen(
                message = state.message
            )
        }
    }
}