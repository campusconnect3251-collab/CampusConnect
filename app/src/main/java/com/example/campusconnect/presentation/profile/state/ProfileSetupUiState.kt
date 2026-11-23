package com.example.campusconnect.presentation.profile.state

data class ProfileSetupUiState(
    val name: String = "",
    val departmentId: String = "",
    val courseId: String = "",
    val admissionYear: String = "",
    val currentYear: String = "",
    val section: String = "",
    val bio: String = "",
    val isLoading: Boolean = false,
    val isProfileCreated: Boolean = false,
    val errorMessage: String? = null
)
