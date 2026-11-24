package com.example.campusconnect.data.model

import com.google.firebase.Timestamp

data class ChatDto(
    val id: String = "",
    val participantIds: List<String> = emptyList(),
    val createdAt: Timestamp? = null,
    val lastMessage: String? = null,
    val lastMessageAt: Timestamp? = null,
    val lastMessageSenderId: String? = null
)
