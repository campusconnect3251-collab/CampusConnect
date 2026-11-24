package com.example.campusconnect.data.datasource.remote

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.campusconnect.data.mapper.toDomain
import com.example.campusconnect.data.mapper.toDto
import com.example.campusconnect.data.model.ChatDto
import com.example.campusconnect.data.model.MessageDto
import com.example.campusconnect.domain.model.Chat
import com.example.campusconnect.domain.model.Message
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.time.Instant
import java.util.Date

@Singleton
class FirebaseChatDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val chatsCollection =
        firestore.collection("chats")

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getOrCreateChat(
        currentUserId: String,
        otherUserId: String
    ): Chat {

        require(currentUserId != otherUserId) {
            "You cannot create a chat with yourself."
        }

        val participantIds = listOf(
            currentUserId,
            otherUserId
        ).sorted()

        val chatId = participantIds.joinToString("_")
        val chatRef = chatsCollection.document(chatId)

        return firestore.runTransaction { transaction ->

            val snapshot = transaction.get(chatRef)

            if (snapshot.exists()) {
                val dto = snapshot.toObject(ChatDto::class.java)
                    ?: error("Unable to read chat.")

                dto.copy(id = snapshot.id).toDomain()

            } else {

                val now = Instant.now()

                val chat = Chat(
                    id = chatId,
                    participantIds = participantIds,
                    createdAt = now,
                    lastMessage = null,
                    lastMessageAt = now,
                    lastMessageSenderId = null
                )

                transaction.set(chatRef, chat.toDto())

                chat
            }

        }.await()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun sendMessage(
        chatId: String,
        senderId: String,
        text: String
    ) {
        require(text.isNotBlank()) {
            "Message cannot be empty."
        }

        val chatRef = chatsCollection.document(chatId)

        val messageRef = chatRef
            .collection("messages")
            .document()

        val now = Instant.now()

        val message = Message(
            id = messageRef.id,
            senderId = senderId,
            text = text.trim(),
            sentAt = now
        )

        val batch = firestore.batch()

        batch.set(
            messageRef,
            message.toDto()
        )

        batch.update(
            chatRef,
            mapOf(
                "lastMessage" to message.text,
                "lastMessageAt" to Timestamp(Date.from(now)),
                "lastMessageSenderId" to senderId
            )
        )

        batch.commit().await()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun observeMessages(
        chatId: String
    ): Flow<List<Message>> = callbackFlow {

        val listener = chatsCollection
            .document(chatId)
            .collection("messages")
            .orderBy("sentAt", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val messages = snapshot?.documents?.mapNotNull { document ->

                    document.toObject(MessageDto::class.java)
                        ?.copy(id = document.id)
                        ?.toDomain()

                } ?: emptyList()

                trySend(messages)
            }

        awaitClose {
            listener.remove()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun observeChats(
        userId: String
    ): Flow<List<Chat>> = callbackFlow {

        val listener = chatsCollection
            .whereArrayContains("participantIds", userId)
            .orderBy("lastMessageAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val chats = snapshot?.documents?.mapNotNull { document ->

                    document.toObject(ChatDto::class.java)
                        ?.copy(id = document.id)
                        ?.toDomain()

                } ?: emptyList()

                trySend(chats)
            }

        awaitClose {
            listener.remove()
        }
    }
}