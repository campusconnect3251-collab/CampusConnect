package com.example.campusconnect.presentation.auth.state

data class AuthUiState(
    val isLoginLoading: Boolean = false,
    val isRegisterLoading: Boolean = false,
    val isGoogleLoading: Boolean = false,
    val isSignOutLoading: Boolean = false,
    val errorMessage: String? = null
)
