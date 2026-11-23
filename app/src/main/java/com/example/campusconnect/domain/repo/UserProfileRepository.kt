package com.example.campusconnect.domain.repo

import com.example.campusconnect.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {

    suspend fun createProfile(
        profile: UserProfile
    ): Result<Unit>

    suspend fun getProfile(
        uid: String
    ): Result<UserProfile?>

    fun observeProfile(
        uid: String
    ): Flow<UserProfile?>

    suspend fun updateProfile(
        profile: UserProfile
    ): Result<Unit>
}