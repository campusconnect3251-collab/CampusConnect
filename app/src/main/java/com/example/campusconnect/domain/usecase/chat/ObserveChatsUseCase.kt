package com.example.campusconnect.domain.usecase.chat

import com.example.campusconnect.domain.model.Chat
import com.example.campusconnect.domain.repo.ChatRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveChatsUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(
        userId: String
    ): Flow<List<Chat>> {
        return chatRepository.observeChats(userId)
    }
}