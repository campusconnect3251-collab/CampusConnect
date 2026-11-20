package com.example.campusconnect.domain.model

import kotlin.time.Instant


data class UserProfile(
    // Basic identity
    val uid: String,
    val name: String,
    val email: String,
    val profileImageUrl: String? = null,
    val bio: String? = null,

    // Academic information
    val departmentId: String? = null,
    val courseId: String? = null,
    val admissionYear: Int? = null,
    val currentYear: Int? = null,
    val section: String? = null,

    // Account information
    val role: UserRole = UserRole.STUDENT,
    val accountStatus: AccountStatus = AccountStatus.ACTIVE,

    // Contact information
    val phoneNumber: String? = null,

    // Timestamps
    val createdAt: Instant,
    val updatedAt: Instant
)
