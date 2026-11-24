package com.example.campusconnect.data.mapper

import com.example.campusconnect.data.model.UserPrivateDto
import com.example.campusconnect.domain.model.UserProfile

fun UserProfile.toPrivateDto(): UserPrivateDto {
    return UserPrivateDto(
        phoneNumber = phoneNumber
    )
}