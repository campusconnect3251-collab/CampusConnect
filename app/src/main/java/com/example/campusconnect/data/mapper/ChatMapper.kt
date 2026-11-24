package com.example.campusconnect.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.campusconnect.data.model.ChatDto
import com.example.campusconnect.domain.model.Chat
import com.google.firebase.Timestamp
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
fun ChatDto.toDomain(): Chat {
    return Chat(
        id = id,
        participantIds = participantIds,
        createdAt = requireNotNull(createdAt) {
            "Chat is missing createdAt"
        }.toDate().toInstant(),

        lastMessage = lastMessage,

        lastMessageAt = lastMessageAt
            ?.toDate()
            ?.toInstant(),

        lastMessageSenderId = lastMessageSenderId
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun Chat.toDto(): ChatDto {
    return ChatDto(
        id = id,
        participantIds = participantIds,
        createdAt = Timestamp(
            Date.from(createdAt)
        ),
        lastMessage = lastMessage,
        lastMessageAt = lastMessageAt?.let {
            Timestamp(Date.from(it))
        },
        lastMessageSenderId = lastMessageSenderId
    )
}