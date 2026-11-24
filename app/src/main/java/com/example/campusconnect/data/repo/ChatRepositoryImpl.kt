package com.example.campusconnect.data.repo

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.campusconnect.data.datasource.remote.FirebaseChatDataSource
import com.example.campusconnect.domain.model.Chat
import com.example.campusconnect.domain.model.Message
import com.example.campusconnect.domain.repo.ChatRepository
import com.example.campusconnect.util.safeCall
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ChatRepositoryImpl @Inject constructor(
    private val chatDataSource: FirebaseChatDataSource
) : ChatRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getOrCreateChat(
        currentUserId: String,
        otherUserId: String
    ): Result<Chat> = safeCall {
        chatDataSource.getOrCreateChat(
            currentUserId = currentUserId,
            otherUserId = otherUserId
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun sendMessage(
        chatId: String,
        senderId: String,
        text: String
    ): Result<Unit> = safeCall {
        chatDataSource.sendMessage(
            chatId = chatId,
            senderId = senderId,
            text = text
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun observeMessages(
        chatId: String
    ): Flow<List<Message>> {
        return chatDataSource.observeMessages(chatId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun observeChats(
        userId: String
    ): Flow<List<Chat>> {
        return chatDataSource.observeChats(userId)
    }
}