package com.example.campusconnect.domain.repo

import com.example.campusconnect.domain.model.AuthUser
import kotlinx.coroutines.flow.Flow


interface AuthRepository {

    suspend fun registerWithEmail(
        email: String,
        password: String
    ): Result<AuthUser>

    suspend fun loginWithEmail(
        email: String,
        password: String
    ): Result<AuthUser>

    suspend fun signInWithGoogle(
        idToken: String
    ): Result<AuthUser>

    suspend fun signOut(): Result<Unit>

    fun getCurrentUser(): AuthUser?

    fun observeAuthState(): Flow<AuthUser?>
}
