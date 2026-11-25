package com.example.campusconnect.presentation.chats.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.campusconnect.presentation.chats.state.ChatConversationUi
import com.example.campusconnect.ui.theme.CampusColors
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatConversationItem(
    conversation: ChatConversationUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile image or initials
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(CampusColors.FieldBorder),
            contentAlignment = Alignment.Center
        ) {
            if (!conversation.profileImageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = conversation.profileImageUrl,
                    contentDescription = "${conversation.name}'s profile picture",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(
                    text = conversation.name
                        .firstOrNull()
                        ?.uppercase() ?: "?",
                    color = CampusColors.Primary,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Conversation details
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = conversation.name,
                color = CampusColors.TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = conversation.lastMessage ?: "Start a conversation",
                color = CampusColors.TextSecondary,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Last message time
        Text(
            text = conversation.lastMessageAt?.let(::formatChatTime) ?: "",
            color = CampusColors.TextSecondary,
            fontSize = 11.sp
        )
    }
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
        thickness = 1.dp,
        color = CampusColors.FieldBorder
    )
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatChatTime(instant: Instant): String {
    val messageTime = instant.atZone(ZoneId.systemDefault())
    val today = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()

    return when (messageTime.toLocalDate()) {
        today -> messageTime.format(DateTimeFormatter.ofPattern("h:mm a"))
        today.minusDays(1) -> "Yesterday"
        else -> messageTime.format(DateTimeFormatter.ofPattern("dd/MM/yy"))
    }
}
