package com.example.campusconnect.domain.repo

import com.example.campusconnect.domain.model.Chat
import com.example.campusconnect.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun getOrCreateChat(
        currentUserId: String,
        otherUserId: String
    ): Result<Chat>

    suspend fun sendMessage(
        chatId: String,
        senderId: String,
        text: String
    ): Result<Unit>

    fun observeMessages(
        chatId: String
    ): Flow<List<Message>>

    fun observeChats(
        userId: String
    ): Flow<List<Chat>>
}