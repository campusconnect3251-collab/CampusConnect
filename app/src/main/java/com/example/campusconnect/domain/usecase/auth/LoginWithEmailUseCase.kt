package com.example.campusconnect.domain.usecase.auth

import com.example.campusconnect.domain.model.AuthUser
import com.example.campusconnect.domain.repo.AuthRepository
import jakarta.inject.Inject


class LoginWithEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<AuthUser> {

        return authRepository.loginWithEmail(
            email = email,
            password = password
        )
    }
}
