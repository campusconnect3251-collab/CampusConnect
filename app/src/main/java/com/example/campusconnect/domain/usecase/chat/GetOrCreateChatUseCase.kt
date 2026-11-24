package com.example.campusconnect.domain.usecase.chat

import com.example.campusconnect.domain.model.Chat
import com.example.campusconnect.domain.repo.ChatRepository
import jakarta.inject.Inject

class GetOrCreateChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        currentUserId: String,
        otherUserId: String
    ): Result<Chat> {
        return chatRepository.getOrCreateChat(
            currentUserId = currentUserId,
            otherUserId = otherUserId
        )
    }
}