package com.example.campusconnect.data.mapper

import com.example.campusconnect.domain.model.AuthUser
import com.google.firebase.auth.FirebaseUser


fun FirebaseUser.toDomain(): AuthUser {
    return AuthUser(
        uid = uid,
        email = email,
        displayName = displayName,
        photoUrl = photoUrl?.toString(),
        isEmailVerified = isEmailVerified
    )
}
