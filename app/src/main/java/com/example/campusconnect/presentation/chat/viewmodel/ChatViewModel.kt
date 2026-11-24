package com.example.campusconnect.presentation.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusconnect.domain.usecase.auth.GetCurrentUserUseCase
import com.example.campusconnect.domain.usecase.chat.ObserveMessagesUseCase
import com.example.campusconnect.domain.usecase.chat.SendMessageUseCase
import com.example.campusconnect.domain.usecase.profile.GetUserProfileUseCase
import com.example.campusconnect.presentation.chat.state.ChatUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val observeMessagesUseCase: ObserveMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    private var currentUserId: String? = null
    private var observedChatId: String? = null

    fun loadChat(
        chatId: String,
        otherUserId: String
    ) {
        if (observedChatId == chatId) return

        observedChatId = chatId

        viewModelScope.launch {

            // 1. Get the currently authenticated user.
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

            currentUserId = currentUser.uid

            _uiState.update {
                it.copy(currentUserId = currentUser.uid)
            }

            // 2. Retrieve the other participant's profile.
            getUserProfileUseCase(otherUserId)
                .onSuccess { profile ->
                    _uiState.update {
                        it.copy(otherUser = profile)
                    }
                }
                .onFailure { exception ->
                    if (exception is CancellationException) throw exception

                    // Message loading can continue even if profile
                    // retrieval fails.
                }

            // 3. Observe messages in real time.
            observeMessagesUseCase(chatId)
                .catch { exception ->
                    if (exception is CancellationException) throw exception

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.localizedMessage
                                ?: "Unable to load messages."
                        )
                    }
                }
                .collect { messages ->
                    _uiState.update {
                        it.copy(
                            messages = messages,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    fun onMessageChange(text: String) {
        _uiState.update {
            it.copy(messageText = text)
        }
    }

    fun sendMessage(chatId: String) {
        val text = _uiState.value.messageText.trim()
        val senderId = currentUserId ?: return

        if (text.isBlank() || _uiState.value.isSending) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSending = true,
                    errorMessage = null
                )
            }

            sendMessageUseCase(
                chatId = chatId,
                senderId = senderId,
                text = text
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        messageText = "",
                        isSending = false
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isSending = false,
                        errorMessage = exception.localizedMessage
                            ?: "Unable to send message."
                    )
                }
            }
        }
    }
}
