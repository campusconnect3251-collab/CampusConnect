package com.example.campusconnect.domain.usecase.profile

import com.example.campusconnect.domain.model.UserProfile
import com.example.campusconnect.domain.repo.UserProfileRepository
import jakarta.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(
        uid: String
    ): Result<UserProfile?> {
        return repository.getProfile(uid)
    }
}