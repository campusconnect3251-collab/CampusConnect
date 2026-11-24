package com.example.campusconnect.data.model

import com.google.firebase.Timestamp

data class MessageDto(
    val id: String = "",
    val senderId: String = "",
    val text: String = "",
    val sentAt: Timestamp? = null
)