package com.example.campusconnect.presentation.people.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusconnect.domain.model.Chat
import com.example.campusconnect.domain.usecase.auth.GetCurrentUserUseCase
import com.example.campusconnect.domain.usecase.chat.GetOrCreateChatUseCase
import com.example.campusconnect.domain.usecase.user.GetUsersUseCase
import com.example.campusconnect.presentation.people.state.PeopleUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class PeopleViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val getOrCreateChatUseCase: GetOrCreateChatUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PeopleUiState())
    val uiState = _uiState.asStateFlow()

    private val _selectedChat = MutableStateFlow<Chat?>(null)
    val selectedChat = _selectedChat.asStateFlow()

    init {
        observeUsers()
    }

    private fun observeUsers() {
        viewModelScope.launch {
            val currentUser = getCurrentUserUseCase()

            if (currentUser == null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Unable to retrieve current user."
                    )
                }
                return@launch
            }

            getUsersUseCase(currentUser.uid)
                .catch { exception ->
                    if (exception is CancellationException) throw exception

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.localizedMessage
                                ?: "Unable to load users."
                        )
                    }
                }
                .collect { users ->
                    _uiState.update {
                        it.copy(
                            users = users,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update {
            it.copy(searchQuery = query)
        }
    }

    fun openChat(otherUserId: String) {
        viewModelScope.launch {
            val currentUser = getCurrentUserUseCase() ?: return@launch

            getOrCreateChatUseCase(
                currentUserId = currentUser.uid,
                otherUserId = otherUserId
            ).onSuccess { chat ->
                _selectedChat.value = chat
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        errorMessage = exception.localizedMessage
                            ?: "Unable to open conversation."
                    )
                }
            }
        }
    }

    fun clearSelectedChat() {
        _selectedChat.value = null
    }
}