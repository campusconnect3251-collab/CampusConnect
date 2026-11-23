package com.example.campusconnect.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.campusconnect.data.model.UserProfileDto
import com.example.campusconnect.domain.model.AccountStatus
import com.example.campusconnect.domain.model.UserProfile
import com.example.campusconnect.domain.model.UserRole
import com.google.firebase.Timestamp
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
fun UserProfile.toDto(): UserProfileDto {
    return UserProfileDto(
        uid = uid,
        name = name,
        email = email,
        profileImageUrl = profileImageUrl,
        bio = bio,
        departmentId = departmentId,
        courseId = courseId,
        admissionYear = admissionYear?.toLong(),
        currentYear = currentYear?.toLong(),
        section = section,
        role = role.name,
        accountStatus = accountStatus.name,
        createdAt = Timestamp(Date.from(createdAt)),
        updatedAt = Timestamp(Date.from(updatedAt))
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun UserProfileDto.toDomain(): UserProfile {
    return UserProfile(
        uid = uid,
        name = name,
        email = email,
        profileImageUrl = profileImageUrl,
        bio = bio,
        departmentId = departmentId,
        courseId = courseId,
        admissionYear = admissionYear?.toInt(),
        currentYear = currentYear?.toInt(),
        section = section,
        role = runCatching {
            UserRole.valueOf(role)
        }.getOrDefault(UserRole.STUDENT),
        accountStatus = runCatching {
            AccountStatus.valueOf(accountStatus)
        }.getOrDefault(AccountStatus.ACTIVE),
        createdAt = requireNotNull(createdAt) {
            "User profile is missing createdAt"
        }.toDate().toInstant(),
        updatedAt = requireNotNull(updatedAt) {
            "User profile is missing updatedAt"
        }.toDate().toInstant()
    )
}