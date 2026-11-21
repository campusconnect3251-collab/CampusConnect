package com.example.campusconnect.domain.usecase.auth

import com.example.campusconnect.domain.model.AuthUser
import com.example.campusconnect.domain.repo.AuthRepository
import jakarta.inject.Inject


class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    operator fun invoke(): AuthUser? {
        return authRepository.getCurrentUser()
    }
}
