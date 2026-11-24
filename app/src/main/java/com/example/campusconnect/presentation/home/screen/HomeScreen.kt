package com.example.campusconnect.presentation.home.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.campusconnect.presentation.home.components.HomeComingUp
import com.example.campusconnect.presentation.home.components.HomeHeader
import com.example.campusconnect.presentation.home.components.HomeNoticeBoard
import com.example.campusconnect.presentation.home.components.HomeQuickAccess
import com.example.campusconnect.presentation.home.components.HomeRecentConversations
import com.example.campusconnect.presentation.home.components.HomeSearchBar
import com.example.campusconnect.presentation.home.viewmodel.HomeViewModel
import com.example.campusconnect.ui.theme.CampusColors

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    onPeopleClick: () -> Unit,
    onChatSelected: (String, String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = CampusColors.Background
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                horizontal = 20.dp,
                vertical = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // Greeting and profile avatar
            item {
                HomeHeader(
                    name = uiState.greetingName,
                    profileImageUrl = uiState.profileImageUrl
                )
            }

            // Search bar
            item {
                HomeSearchBar()
            }

            item {
                HomeQuickAccess()
            }

            item {
                HomeRecentConversations(
                    conversations = uiState.conversations,
                    onConversationClick = { chatId, otherUserId ->
                        onChatSelected(chatId, otherUserId)
                    },
                    onSeeAllClick = onPeopleClick
                )
            }

            item {
                HomeComingUp()
            }

            item {
                HomeNoticeBoard()
            }
        }
    }
}
