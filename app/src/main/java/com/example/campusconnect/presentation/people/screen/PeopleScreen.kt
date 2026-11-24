package com.example.campusconnect.presentation.people.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.campusconnect.presentation.people.viewmodel.PeopleViewModel
import com.example.campusconnect.presentation.people.components.PersonItem
import com.example.campusconnect.ui.theme.CampusColors

@Composable
fun PeopleScreen(
    onBackClick: () -> Unit,
    onChatSelected: (chatId: String, otherUserId: String) -> Unit,
    viewModel: PeopleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedChat by viewModel.selectedChat.collectAsStateWithLifecycle()

    var selectedUserId by remember { mutableStateOf<String?>(null) }

    // Navigate when the ViewModel successfully creates or retrieves a chat.
    LaunchedEffect(selectedChat) {
        val chat = selectedChat
        val userId = selectedUserId

        if (chat != null && userId != null) {
            onChatSelected(chat.id, userId)
            viewModel.clearSelectedChat()
            selectedUserId = null
        }
    }

    val filteredUsers = remember(uiState.users, uiState.searchQuery) {
        uiState.users.filter { user ->
            user.name.contains(uiState.searchQuery, ignoreCase = true) ||
                    user.email.contains(uiState.searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        containerColor = CampusColors.Background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {

            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go back",
                        tint = CampusColors.TextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "People",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = CampusColors.TextPrimary
                )
            }

            Text(
                text = "Connect with your campus",
                style = MaterialTheme.typography.bodyMedium,
                color = CampusColors.TextSecondary
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Search field
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Search by name or email",
                        color = CampusColors.TextSecondary
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = CampusColors.TextSecondary
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CampusColors.Primary,
                    unfocusedBorderColor = CampusColors.FieldBorder,
                    focusedTextColor = CampusColors.TextPrimary,
                    unfocusedTextColor = CampusColors.TextPrimary,
                    cursorColor = CampusColors.Primary,
                    focusedContainerColor = CampusColors.White,
                    unfocusedContainerColor = CampusColors.White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "CAMPUS MEMBERS",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = CampusColors.TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = CampusColors.Primary
                        )
                    }
                }

                uiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.errorMessage.orEmpty(),
                            color = CampusColors.Error
                        )
                    }
                }

                filteredUsers.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (uiState.searchQuery.isBlank()) {
                                "No other campus members found."
                            } else {
                                "No members match your search."
                            },
                            color = CampusColors.TextSecondary
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {
                        items(
                            items = filteredUsers,
                            key = { it.uid }
                        ) { user ->
                            PersonItem(
                                user = user,
                                onClick = {
                                    selectedUserId = user.uid
                                    viewModel.openChat(user.uid)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}