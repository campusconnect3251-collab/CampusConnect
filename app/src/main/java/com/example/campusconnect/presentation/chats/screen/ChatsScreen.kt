package com.example.campusconnect.presentation.chats.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.campusconnect.presentation.chats.components.ChatConversationItem
import com.example.campusconnect.presentation.chats.components.ChatsFilterRow
import com.example.campusconnect.presentation.chats.components.ChatsHeader
import com.example.campusconnect.presentation.chats.components.ChatsSearchBar
import com.example.campusconnect.presentation.chats.viewmodel.ChatsViewModel
import com.example.campusconnect.ui.theme.CampusColors

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatsScreen(
    onNewChatClick: () -> Unit,
    onChatClick: (String, String) -> Unit,
    viewModel: ChatsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            ChatsHeader(
                onNewChatClick = onNewChatClick
            )

            Spacer(modifier = Modifier.height(20.dp))

            ChatsSearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChange
            )

            Spacer(modifier = Modifier.height(16.dp))

            ChatsFilterRow(
                selectedFilter = uiState.selectedFilter,
                onFilterSelected = viewModel::onFilterSelected
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = CampusColors.Primary
                        )
                    }
                }

                uiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.errorMessage ?: "Something went wrong.",
                            color = CampusColors.Error
                        )
                    }
                }

                uiState.conversations.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (uiState.searchQuery.isNotBlank()) {
                                "No conversations found."
                            } else {
                                "No conversations yet. Start a new chat!"
                            },
                            color = CampusColors.TextSecondary
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(
                            items = uiState.conversations,
                            key = { it.chatId }
                        ) { conversation ->

                            ChatConversationItem(
                                conversation = conversation,
                                onClick = {
                                    onChatClick(
                                        conversation.chatId,
                                        conversation.otherUserId
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = onNewChatClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = CampusColors.Primary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = CampusColors.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}