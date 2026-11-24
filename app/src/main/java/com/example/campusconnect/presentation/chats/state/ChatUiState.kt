package com.example.campusconnect.presentation.chats.state

import java.time.Instant

enum class ChatsFilter {
    ALL,
    UNREAD,
    GROUPS
}

data class ChatConversationUi(
    val chatId: String,
    val otherUserId: String,
    val name: String,
    val profileImageUrl: String?,
    val lastMessage: String?,
    val lastMessageAt: Instant?,
    val unreadCount: Int = 0,
    val isGroup: Boolean = false
)

data class ChatsUiState(
    val allConversations: List<ChatConversationUi> = emptyList(),
    val conversations: List<ChatConversationUi> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: ChatsFilter = ChatsFilter.ALL,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)