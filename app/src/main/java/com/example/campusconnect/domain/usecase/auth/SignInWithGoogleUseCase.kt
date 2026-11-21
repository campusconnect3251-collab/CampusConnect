package com.example.campusconnect.domain.usecase.auth

import com.example.campusconnect.domain.model.AuthUser
import com.example.campusconnect.domain.repo.AuthRepository
import jakarta.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        idToken: String
    ): Result<AuthUser> {

        return authRepository.signInWithGoogle(idToken)
    }
}