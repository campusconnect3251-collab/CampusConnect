package com.example.campusconnect.presentation.auth.state

import com.example.campusconnect.domain.model.AuthUser

sealed interface AuthState {
    data object Loading : AuthState

    data class Authenticated(
        val user: AuthUser
    ) : AuthState

    data object Unauthenticated : AuthState
}