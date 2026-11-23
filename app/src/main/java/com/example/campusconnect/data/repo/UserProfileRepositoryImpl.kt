package com.example.campusconnect.data.repo

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.campusconnect.data.datasource.remote.FirebaseUserProfileDataSource
import com.example.campusconnect.data.mapper.toDomain
import com.example.campusconnect.data.mapper.toDto
import com.example.campusconnect.domain.model.UserProfile
import com.example.campusconnect.domain.repo.UserProfileRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.cancellation.CancellationException

class UserProfileRepositoryImpl @Inject constructor(
    private val dataSource: FirebaseUserProfileDataSource
) : UserProfileRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun createProfile(
        profile: UserProfile
    ): Result<Unit> = safeCall {
        dataSource.createProfile(profile.toDto())
    }

    override suspend fun getProfile(
        uid: String
    ): Result<UserProfile?> = safeCall {
        dataSource.getProfile(uid)?.toDomain()
    }

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

    private suspend fun <T> safeCall(
        block: suspend () -> T
    ): Result<T> {
        return try {
            Result.success(block())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}