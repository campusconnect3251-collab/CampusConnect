package com.example.campusconnect.data.repo

import com.example.campusconnect.data.datasource.remote.FirebaseAuthDataSource
import com.example.campusconnect.data.mapper.toDomain
import com.example.campusconnect.domain.model.AuthUser
import com.example.campusconnect.domain.repo.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.cancellation.CancellationException


class FirebaseAuthRepository @Inject constructor(
    private val authDataSource: FirebaseAuthDataSource
) : AuthRepository {

    override suspend fun registerWithEmail(
        email: String,
        password: String
    ): Result<AuthUser> = safeCall {
        authDataSource
            .registerWithEmail(email, password)
            .toDomain()
    }

    override suspend fun loginWithEmail(
        email: String,
        password: String
    ): Result<AuthUser> = safeCall {
        authDataSource
            .loginWithEmail(email, password)
            .toDomain()
    }

    override suspend fun signInWithGoogle(
        idToken: String
    ): Result<AuthUser> = safeCall {
        authDataSource
            .signInWithGoogle(idToken)
            .toDomain()
    }

    override suspend fun signOut(): Result<Unit> = safeCall {
        authDataSource.signOut()
    }

    override fun getCurrentUser(): AuthUser? {
        return authDataSource
            .getCurrentUser()
            ?.toDomain()
    }

    override fun observeAuthState(): Flow<AuthUser?> {
        return authDataSource
            .observeAuthState()
            .map { firebaseUser ->
                firebaseUser?.toDomain()
            }
    }

    private suspend fun <T> safeCall(
        action: suspend () -> T
    ): Result<T> {
        return try {
            Result.success(action())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
