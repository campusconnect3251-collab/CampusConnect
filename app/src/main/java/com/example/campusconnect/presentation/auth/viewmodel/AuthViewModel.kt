package com.example.campusconnect.presentation.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusconnect.domain.usecase.auth.LoginWithEmailUseCase
import com.example.campusconnect.domain.usecase.auth.ObserveAuthStateUseCase
import com.example.campusconnect.domain.usecase.auth.RegisterWithEmailUseCase
import com.example.campusconnect.domain.usecase.auth.SignInWithGoogleUseCase
import com.example.campusconnect.domain.usecase.auth.SignOutUseCase
import com.example.campusconnect.presentation.auth.state.AuthState
import com.example.campusconnect.presentation.auth.state.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registerWithEmailUseCase: RegisterWithEmailUseCase,
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val observeAuthStateUseCase: ObserveAuthStateUseCase
) : ViewModel() {

    // Overall authentication state
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState = _authState.asStateFlow()

    // Authentication screen UI state
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            observeAuthStateUseCase().collect { user ->
                _authState.value = if (user != null) {
                    AuthState.Authenticated(user)
                } else {
                    AuthState.Unauthenticated
                }
            }
        }
    }

    fun registerWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isRegisterLoading = true,
                    errorMessage = null
                )
            }

            val result = registerWithEmailUseCase(email, password)

            result.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        errorMessage = exception.message
                            ?: "Registration failed"
                    )
                }
            }

            _uiState.update {
                it.copy(isRegisterLoading = false)
            }
        }
    }

    fun loginWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoginLoading = true,
                    errorMessage = null
                )
            }

            val result = loginWithEmailUseCase(email, password)

            result.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        errorMessage = exception.message
                            ?: "Login failed"
                    )
                }
            }

            _uiState.update {
                it.copy(isLoginLoading = false)
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isGoogleLoading = true,
                    errorMessage = null
                )
            }

            val result = signInWithGoogleUseCase(idToken)

            result.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        errorMessage = exception.message
                            ?: "Google sign-in failed"
                    )
                }
            }

            _uiState.update {
                it.copy(isGoogleLoading = false)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSignOutLoading = true,
                    errorMessage = null
                )
            }

            val result = signOutUseCase()

            result.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        errorMessage = exception.message
                            ?: "Sign out failed"
                    )
                }
            }

            _uiState.update {
                it.copy(isSignOutLoading = false)
            }
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }
}
