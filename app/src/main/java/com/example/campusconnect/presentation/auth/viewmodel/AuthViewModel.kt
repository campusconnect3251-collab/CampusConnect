package com.example.campusconnect.presentation.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusconnect.domain.usecase.auth.LoginWithEmailUseCase
import com.example.campusconnect.domain.usecase.auth.ObserveAuthStateUseCase
import com.example.campusconnect.domain.usecase.auth.RegisterWithEmailUseCase
import com.example.campusconnect.domain.usecase.auth.SignInWithGoogleUseCase
import com.example.campusconnect.domain.usecase.auth.SignOutUseCase
import com.example.campusconnect.domain.usecase.profile.ObserveUserProfileUseCase
import com.example.campusconnect.presentation.auth.state.AuthState
import com.example.campusconnect.presentation.auth.state.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registerWithEmailUseCase: RegisterWithEmailUseCase,
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val observeUserProfileUseCase: ObserveUserProfileUseCase
) : ViewModel() {

    // Overall authentication state
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState = _authState.asStateFlow()

    // Authentication screen UI state
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    init {
        Log.d("AuthViewModel", "AuthViewModel initialized")
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            observeAuthStateUseCase()
                .collectLatest { user ->
                    Log.d("AuthViewModel", "Auth user UID: ${user?.uid}")
                    if (user == null) {
                        _authState.value = AuthState.Unauthenticated
                        return@collectLatest
                    }

                    // User is authenticated. Now check their profile.
                    _authState.value = AuthState.Loading

                    observeUserProfileUseCase(user.uid)
                        .catch { exception ->
                            Log.e("AuthViewModel", "Profile observation failed", exception)
                            if (exception is CancellationException) {
                                throw exception
                            }

                            _authState.value =
                                AuthState.ProfileCheckFailed(
                                    user = user,
                                    message = exception.localizedMessage
                                        ?: "Unable to check your profile."
                                )
                        }
                        .collect { profile ->
                            Log.d("AuthViewModel", "Observed profile: $profile")
                            _authState.value = if (profile == null) {
                                Log.d("AuthViewModel", "Profile setup required")
                                AuthState.ProfileSetupRequired(user)
                            } else {
                                AuthState.Authenticated(
                                    user = user,
                                    profile = profile
                                )
                            }
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
