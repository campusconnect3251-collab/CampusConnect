package com.example.campusconnect.presentation.chats.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusconnect.domain.usecase.auth.GetCurrentUserUseCase
import com.example.campusconnect.domain.usecase.chat.ObserveChatsUseCase
import com.example.campusconnect.domain.usecase.profile.GetUserProfileUseCase
import com.example.campusconnect.presentation.chats.state.ChatConversationUi
import com.example.campusconnect.presentation.chats.state.ChatsFilter
import com.example.campusconnect.presentation.chats.state.ChatsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val observeChatsUseCase: ObserveChatsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadChats()
    }

    private fun loadChats() {
        viewModelScope.launch {
            try {
                val currentUser = getCurrentUserUseCase()

                if (currentUser == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Unable to identify the current user."
                        )
                    }
                    return@launch
                }

                observeChatsUseCase(currentUser.uid)
                    .catch { exception ->
                        if (exception is CancellationException) throw exception

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = exception.localizedMessage
                                    ?: "Unable to load conversations."
                            )
                        }
                    }
                    .collect { chats ->

                        val conversations = coroutineScope {
                            chats.map { chat ->
                                async {
                                    val otherUserIds = chat.participantIds
                                        .filter { it != currentUser.uid }

                                    if (otherUserIds.isEmpty()) {
                                        return@async null
                                    }

                                    val isGroup = chat.participantIds.size > 2

                                    // For now, display the first other participant.
                                    val otherUserId = otherUserIds.first()

                                    val profile = getUserProfileUseCase(otherUserId)
                                        .getOrNull()

                                    ChatConversationUi(
                                        chatId = chat.id,
                                        otherUserId = otherUserId,
                                        name = if (isGroup) {
                                            "Group conversation"
                                        } else {
                                            profile?.name ?: "Unknown user"
                                        },
                                        profileImageUrl = profile?.profileImageUrl,
                                        lastMessage = chat.lastMessage,
                                        lastMessageAt = chat.lastMessageAt,
                                        isGroup = isGroup
                                    )
                                }
                            }.awaitAll().filterNotNull()
                        }

                        _uiState.update {
                            val updatedState = it.copy(
                                allConversations = conversations,
                                isLoading = false,
                                errorMessage = null
                            )

                            applyFilters(updatedState)
                        }
                    }

            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.localizedMessage
                            ?: "Something went wrong."
                    )
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update {
            applyFilters(
                it.copy(searchQuery = query)
            )
        }
    }

    fun onFilterSelected(filter: ChatsFilter) {
        _uiState.update {
            applyFilters(
                it.copy(selectedFilter = filter)
            )
        }
    }

    private fun applyFilters(state: ChatsUiState): ChatsUiState {

        val filtered = state.allConversations.filter { conversation ->

            val matchesSearch =
                conversation.name.contains(
                    state.searchQuery,
                    ignoreCase = true
                ) ||
                        conversation.lastMessage.orEmpty().contains(
                            state.searchQuery,
                            ignoreCase = true
                        )

            val matchesFilter = when (state.selectedFilter) {
                ChatsFilter.ALL -> true

                ChatsFilter.UNREAD ->
                    conversation.unreadCount > 0

                ChatsFilter.GROUPS ->
                    conversation.isGroup
            }

            matchesSearch && matchesFilter
        }

        return state.copy(conversations = filtered)
    }
}