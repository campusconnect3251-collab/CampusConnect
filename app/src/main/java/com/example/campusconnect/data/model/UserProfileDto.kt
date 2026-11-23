package com.example.campusconnect.data.model

import com.google.firebase.Timestamp

data class UserProfileDto(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val profileImageUrl: String? = null,
    val bio: String? = null,
    val departmentId: String? = null,
    val courseId: String? = null,
    val admissionYear: Long? = null,
    val currentYear: Long? = null,
    val section: String? = null,
    val role: String = "STUDENT",
    val accountStatus: String = "ACTIVE",
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)
