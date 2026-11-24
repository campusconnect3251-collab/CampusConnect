package com.example.campusconnect.data.di

import com.example.campusconnect.data.repo.ChatRepositoryImpl
import com.example.campusconnect.data.repo.FirebaseAuthRepository
import com.example.campusconnect.data.repo.UserProfileRepositoryImpl
import com.example.campusconnect.domain.repo.AuthRepository
import com.example.campusconnect.domain.repo.ChatRepository
import com.example.campusconnect.domain.repo.UserProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        firebaseAuthRepository: FirebaseAuthRepository
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserProfileRepository(
        userProfileRepositoryImpl: UserProfileRepositoryImpl
    ): UserProfileRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        chatRepositoryImpl: ChatRepositoryImpl
    ): ChatRepository
}
