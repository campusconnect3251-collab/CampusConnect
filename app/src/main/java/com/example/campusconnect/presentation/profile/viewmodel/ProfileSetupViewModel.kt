package com.example.campusconnect.presentation.profile.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusconnect.domain.model.UserProfile
import com.example.campusconnect.domain.usecase.auth.GetCurrentUserUseCase
import com.example.campusconnect.domain.usecase.profile.CreateProfileUseCase
import com.example.campusconnect.presentation.profile.state.ProfileSetupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(
    private val createProfileUseCase: CreateProfileUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileSetupUiState())
    val uiState = _uiState.asStateFlow()

    fun onNameChange(value: String) {
        _uiState.update {
            it.copy(name = value, errorMessage = null)
        }
    }

    fun onDepartmentChange(value: String) {
        _uiState.update {
            it.copy(departmentId = value, errorMessage = null)
        }
    }

    fun onCourseChange(value: String) {
        _uiState.update {
            it.copy(courseId = value, errorMessage = null)
        }
    }

    fun onAdmissionYearChange(value: String) {
        _uiState.update {
            it.copy(admissionYear = value, errorMessage = null)
        }
    }

    fun onCurrentYearChange(value: String) {
        _uiState.update {
            it.copy(currentYear = value, errorMessage = null)
        }
    }

    fun onSectionChange(value: String) {
        _uiState.update {
            it.copy(section = value, errorMessage = null)
        }
    }

    fun onBioChange(value: String) {
        _uiState.update {
            it.copy(bio = value, errorMessage = null)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveProfile() {
        val state = _uiState.value

        if (state.isLoading) return

        val admissionYear = state.admissionYear.toIntOrNull()
        val currentYear = state.currentYear.toIntOrNull()

        when {
            state.name.isBlank() ->
                updateError("Please enter your full name.")

            state.departmentId.isBlank() ->
                updateError("Please enter your department.")

            state.courseId.isBlank() ->
                updateError("Please enter your course.")

            admissionYear == null || admissionYear <= 0 ->
                updateError("Please enter a valid admission year.")

            currentYear == null || currentYear <= 0 ->
                updateError("Please enter a valid current year.")

            else -> createProfile(
                state = state,
                admissionYear = admissionYear,
                currentYear = currentYear
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createProfile(
        state: ProfileSetupUiState,
        admissionYear: Int,
        currentYear: Int
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            val authUser = getCurrentUserUseCase()

            if (authUser == null || authUser.email.isNullOrBlank()) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Unable to retrieve the authenticated user."
                    )
                }
                return@launch
            }

            val now = Instant.now()

            val profile = UserProfile(
                uid = authUser.uid,
                name = state.name.trim(),
                email = authUser.email,
                profileImageUrl = authUser.photoUrl,
                bio = state.bio.trim().ifBlank { null },
                departmentId = state.departmentId.trim(),
                courseId = state.courseId.trim(),
                admissionYear = admissionYear,
                currentYear = currentYear,
                section = state.section.trim().ifBlank { null },
                createdAt = now,
                updatedAt = now
            )

            createProfileUseCase(profile)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isProfileCreated = true
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.localizedMessage
                                ?: "Unable to save your profile."
                        )
                    }
                }
        }
    }

    private fun updateError(message: String) {
        _uiState.update {
            it.copy(errorMessage = message)
        }
    }
}