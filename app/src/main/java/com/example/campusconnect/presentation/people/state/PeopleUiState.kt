package com.example.campusconnect.presentation.people.state

import com.example.campusconnect.domain.model.UserProfile

data class PeopleUiState(
    val users: List<UserProfile> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)