package com.example.campusconnect.presentation.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
object PeopleRoute

@Serializable
object ChatsRoute

@Serializable
data class ChatRoute(
    val chatId : String,
    val otherUserId : String
)