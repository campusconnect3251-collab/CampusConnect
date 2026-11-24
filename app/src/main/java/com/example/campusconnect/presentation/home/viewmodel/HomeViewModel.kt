package com.example.campusconnect.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusconnect.domain.usecase.auth.GetCurrentUserUseCase
import com.example.campusconnect.domain.usecase.chat.ObserveChatsUseCase
import com.example.campusconnect.domain.usecase.profile.GetUserProfileUseCase
import com.example.campusconnect.presentation.home.state.HomeConversationUi
import com.example.campusconnect.presentation.home.state.HomeUiState
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
class HomeViewModel @Inject constructor(
    private val observeChatsUseCase: ObserveChatsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadHome()
    }

    private fun loadHome() {
        viewModelScope.launch {
            try {
                // 1. Get the currently logged-in user
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

                // 2. Load the current user's profile
                getUserProfileUseCase(currentUser.uid)
                    .onSuccess { profile ->
                        _uiState.update {
                            it.copy(
                                greetingName = profile?.name.orEmpty(),
                                profileImageUrl = profile?.profileImageUrl
                            )
                        }
                    }
                    .onFailure { exception ->
                        if (exception is CancellationException) throw exception
                    }

                // 3. Observe conversations in real time
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
                        // 4. Fetch the other participant's profile
                        val conversations = coroutineScope {
                            chats.map { chat ->
                                async {
                                    val otherUserId = chat.participantIds
                                        .firstOrNull { it != currentUser.uid }
                                        ?: return@async null

                                    val profile = getUserProfileUseCase(otherUserId)
                                        .getOrNull()

                                    HomeConversationUi(
                                        chatId = chat.id,
                                        otherUserId = otherUserId,
                                        name = profile?.name ?: "Unknown user",
                                        profileImageUrl = profile?.profileImageUrl,
                                        lastMessage = chat.lastMessage,
                                        lastMessageAt = chat.lastMessageAt
                                    )
                                }
                            }.awaitAll().filterNotNull()
                        }

                        // 5. Update the UI
                        _uiState.update {
                            it.copy(
                                conversations = conversations,
                                isLoading = false,
                                errorMessage = null
                            )
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
}
