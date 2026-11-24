package com.example.campusconnect.domain.usecase.chat

import com.example.campusconnect.domain.model.Message
import com.example.campusconnect.domain.repo.ChatRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(
        chatId: String
    ): Flow<List<Message>> {
        return chatRepository.observeMessages(chatId)
    }
}