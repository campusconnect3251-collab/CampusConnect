package com.example.campusconnect.domain.usecase.auth

import com.example.campusconnect.domain.repo.AuthRepository
import jakarta.inject.Inject


class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(): Result<Unit> {
        return authRepository.signOut()
    }
}
