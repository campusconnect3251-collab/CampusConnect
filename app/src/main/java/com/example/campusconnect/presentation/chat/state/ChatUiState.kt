package com.example.campusconnect.presentation.chat.state

import com.example.campusconnect.domain.model.Message
import com.example.campusconnect.domain.model.UserProfile

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val messageText: String = "",
    val currentUserId: String? = null,
    val otherUser: UserProfile? = null,
    val isLoading: Boolean = true,
    val isSending: Boolean = false,
    val errorMessage: String? = null
)