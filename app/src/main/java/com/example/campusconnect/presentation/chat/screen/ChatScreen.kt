package com.example.campusconnect.presentation.chat.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.campusconnect.presentation.chat.components.ChatTopBar
import com.example.campusconnect.presentation.chat.components.MessageBubble
import com.example.campusconnect.presentation.chat.components.MessageInput
import com.example.campusconnect.presentation.chat.viewmodel.ChatViewModel
import com.example.campusconnect.ui.theme.CampusColors

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(
    chatId : String,
    otherUserId : String,
    onBackClick: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(chatId, otherUserId) {
        viewModel.loadChat(
            chatId = chatId,
            otherUserId = otherUserId
        )
    }
    Scaffold(
        containerColor = CampusColors.Background,
        contentWindowInsets = WindowInsets.safeDrawing
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            ChatTopBar(
                user = uiState.otherUser,
                onBackClick = onBackClick
            )

            HorizontalDivider(
                color = CampusColors.FieldBorder
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = CampusColors.Primary
                        )
                    }

                    uiState.errorMessage != null &&
                            uiState.messages.isEmpty() -> {
                        Text(
                            text = uiState.errorMessage.orEmpty(),
                            modifier = Modifier.align(Alignment.Center),
                            color = CampusColors.Error
                        )
                    }

                    uiState.messages.isEmpty() -> {
                        Text(
                            text = "No messages yet. Say hello!",
                            modifier = Modifier.align(Alignment.Center),
                            color = CampusColors.TextSecondary
                        )
                    }

                    else -> {
                        val listState = rememberLazyListState()

                        LaunchedEffect(uiState.messages.size) {
                            if (uiState.messages.isNotEmpty()) {
                                listState.animateScrollToItem(
                                    uiState.messages.lastIndex
                                )
                            }
                        }

                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                horizontal = 16.dp,
                                vertical = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(
                                items = uiState.messages,
                                key = { it.id }
                            ) { message ->

                                MessageBubble(
                                    message = message,
                                    isMine = message.senderId == uiState.currentUserId
                                )
                            }
                        }
                    }
                }
            }

            MessageInput(
                value = uiState.messageText,
                onValueChange = viewModel::onMessageChange,
                onSendClick = {
                    viewModel.sendMessage(chatId)
                },
                isSending = uiState.isSending
            )
        }
    }
}