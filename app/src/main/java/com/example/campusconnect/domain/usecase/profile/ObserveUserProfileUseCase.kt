package com.example.campusconnect.domain.usecase.profile

import com.example.campusconnect.domain.model.UserProfile
import com.example.campusconnect.domain.repo.UserProfileRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveUserProfileUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    operator fun invoke(
        uid: String
    ): Flow<UserProfile?> {
        return repository.observeProfile(uid)
    }
}