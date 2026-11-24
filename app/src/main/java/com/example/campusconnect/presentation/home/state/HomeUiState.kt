package com.example.campusconnect.presentation.home.state

import java.time.Instant

data class HomeConversationUi(
    val chatId: String,
    val otherUserId: String,
    val name: String,
    val profileImageUrl: String?,
    val lastMessage: String?,
    val lastMessageAt: Instant?,
    val unreadCount: Int = 0
)

data class HomeUiState(
    val greetingName: String = "",
    val profileImageUrl: String? = null,
    val conversations: List<HomeConversationUi> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)
