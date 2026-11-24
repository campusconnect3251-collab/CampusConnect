package com.example.campusconnect.domain.usecase.chat

import com.example.campusconnect.domain.repo.ChatRepository
import jakarta.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        chatId: String,
        senderId: String,
        text: String
    ): Result<Unit> {

        if (text.isBlank()) {
            return Result.failure(
                IllegalArgumentException("Message cannot be empty.")
            )
        }

        return chatRepository.sendMessage(
            chatId = chatId,
            senderId = senderId,
            text = text.trim()
        )
    }
}