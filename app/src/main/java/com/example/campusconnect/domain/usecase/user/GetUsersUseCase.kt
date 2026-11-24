package com.example.campusconnect.domain.usecase.user

import com.example.campusconnect.domain.model.UserProfile
import com.example.campusconnect.domain.repo.UserProfileRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetUsersUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository
) {
    operator fun invoke(
        currentUserId: String
    ): Flow<List<UserProfile>> {
        return userProfileRepository.observeUsers(currentUserId)
    }
}