package com.example.campusconnect.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.campusconnect.data.model.MessageDto
import com.example.campusconnect.domain.model.Message
import com.google.firebase.Timestamp
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
fun MessageDto.toDomain(): Message {
    return Message(
        id = id,
        senderId = senderId,
        text = text,
        sentAt = requireNotNull(sentAt) {
            "Message is missing sentAt"
        }.toDate().toInstant()
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun Message.toDto(): MessageDto {
    return MessageDto(
        id = id,
        senderId = senderId,
        text = text,
        sentAt = Timestamp(Date.from(sentAt))
    )
}