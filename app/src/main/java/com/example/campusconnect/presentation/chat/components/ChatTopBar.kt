package com.example.campusconnect.presentation.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.campusconnect.domain.model.UserProfile
import com.example.campusconnect.ui.theme.CampusColors

@Composable
fun ChatTopBar(
    user: UserProfile?,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CampusColors.White)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = CampusColors.TextPrimary
            )
        }

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(CampusColors.FieldBorder),
            contentAlignment = Alignment.Center
        ) {
            if (!user?.profileImageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = user.profileImageUrl,
                    contentDescription = "Profile picture",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(
                    text = user?.name?.firstOrNull()?.uppercase() ?: "?",
                    color = CampusColors.Primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = user?.name ?: "Campus Member",
                color = CampusColors.TextPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Campus Connect",
                color = CampusColors.TextSecondary,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
