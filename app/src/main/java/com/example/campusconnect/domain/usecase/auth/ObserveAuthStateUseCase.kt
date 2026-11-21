package com.example.campusconnect.domain.usecase.auth

import com.example.campusconnect.domain.model.AuthUser
import com.example.campusconnect.domain.repo.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    operator fun invoke(): Flow<AuthUser?> {
        return authRepository.observeAuthState()
    }
}