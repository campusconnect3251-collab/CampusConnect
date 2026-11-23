package com.example.campusconnect.presentation.profile.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.campusconnect.ui.theme.CampusColors

@Composable
fun ProfileCheckErrorScreen(
    message: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CampusColors.Background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = CampusColors.Error
        )
    }
}