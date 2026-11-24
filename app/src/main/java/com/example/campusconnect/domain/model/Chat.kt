package com.example.campusconnect.domain.model

import java.time.Instant

data class Chat(
    val id: String,
    val participantIds: List<String>,
    val createdAt: Instant,
    val lastMessage: String?,
    val lastMessageAt: Instant?,
    val lastMessageSenderId: String?
)