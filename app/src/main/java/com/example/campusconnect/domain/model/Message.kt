package com.example.campusconnect.domain.model

import java.time.Instant

data class Message(
    val id: String,
    val senderId: String,
    val text: String,
    val sentAt: Instant
)
