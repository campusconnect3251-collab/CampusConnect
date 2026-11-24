package com.example.campusconnect.data.repo

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.campusconnect.data.datasource.remote.FirebaseUserProfileDataSource
import com.example.campusconnect.data.mapper.toDomain
import com.example.campusconnect.data.mapper.toDto
import com.example.campusconnect.domain.model.UserProfile
import com.example.campusconnect.domain.repo.UserProfileRepository
import com.example.campusconnect.util.safeCall
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserProfileRepositoryImpl @Inject constructor(
    private val dataSource: FirebaseUserProfileDataSource
) : UserProfileRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun createProfile(
        profile: UserProfile
    ): Result<Unit> = safeCall {
        dataSource.createProfile(profile.toDto())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getProfile(
        uid: String
    ): Result<UserProfile?> = safeCall {
        dataSource.getProfile(uid)?.toDomain()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun observeProfile(
        uid: String
    ): Flow<UserProfile?> {
        return dataSource.observeProfile(uid)
            .map { dto -> dto?.toDomain() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updateProfile(
        profile: UserProfile
    ): Result<Unit> = safeCall {
        dataSource.updateProfile(profile.toDto())
    }

}