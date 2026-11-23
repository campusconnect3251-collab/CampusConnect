package com.example.campusconnect.presentation.auth.state

import com.example.campusconnect.domain.model.AuthUser
import com.example.campusconnect.domain.model.UserProfile

sealed interface AuthState {
    data object Loading : AuthState

    data class Authenticated(
        val user: AuthUser,
        val profile: UserProfile
    ) : AuthState

    data object Unauthenticated : AuthState

    data class ProfileSetupRequired(
        val user: AuthUser
    ) : AuthState

    data class ProfileCheckFailed(
        val user: AuthUser,
        val message: String
    ) : AuthState
}