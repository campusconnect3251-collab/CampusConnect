package com.example.campusconnect.presentation.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.campusconnect.presentation.home.state.HomeConversationUi
import com.example.campusconnect.ui.theme.CampusColors
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeRecentConversations(
    conversations: List<HomeConversationUi>,
    onConversationClick: (String, String) -> Unit,
    onSeeAllClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        // Section heading
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent conversations",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CampusColors.TextPrimary
            )

            Text(
                text = "See all",
                style = MaterialTheme.typography.labelLarge,
                color = CampusColors.Primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    onSeeAllClick()
                }
            )
        }

        if (conversations.isEmpty()) {
            EmptyConversations()
        } else {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = CampusColors.White,
                shadowElevation = 2.dp
            ) {
                Column {
                    conversations.take(4).forEachIndexed { index, conversation ->

                        ConversationItem(
                            conversation = conversation,
                            onClick = {
                                onConversationClick(
                                    conversation.chatId,
                                    conversation.otherUserId
                                )
                            }
                        )

                        if (index < conversations.take(4).lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = CampusColors.FieldBorder
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ConversationItem(
    conversation: HomeConversationUi,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Profile picture
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(CampusColors.FieldBorder),
            contentAlignment = Alignment.Center
        ) {
            if (!conversation.profileImageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = conversation.profileImageUrl,
                    contentDescription = "${conversation.name}'s profile picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = conversation.name
                        .split(" ")
                        .filter { it.isNotBlank() }
                        .take(2)
                        .mapNotNull { it.firstOrNull() }
                        .joinToString("")
                        .uppercase()
                        .ifBlank { "U" },
                    color = CampusColors.Primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Name and last message
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = conversation.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = CampusColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = conversation.lastMessage ?: "Start a conversation",
                style = MaterialTheme.typography.bodySmall,
                color = CampusColors.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Last message time
        Text(
            text = formatConversationTime(conversation.lastMessageAt),
            style = MaterialTheme.typography.labelSmall,
            color = CampusColors.TextSecondary
        )
    }
}

@Composable
private fun EmptyConversations() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = CampusColors.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 28.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = CampusColors.Primary,
                modifier = Modifier.size(32.dp)
            )

            Text(
                text = "No conversations yet",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = CampusColors.TextPrimary
            )

            Text(
                text = "Start connecting with people from your campus.",
                style = MaterialTheme.typography.bodySmall,
                color = CampusColors.TextSecondary
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatConversationTime(instant: Instant?): String {
    if (instant == null) return ""

    val zone = ZoneId.systemDefault()
    val messageDate = instant.atZone(zone)
    val today = Instant.now().atZone(zone).toLocalDate()

    return if (messageDate.toLocalDate() == today) {
        messageDate.format(DateTimeFormatter.ofPattern("h:mm a"))
    } else {
        messageDate.format(DateTimeFormatter.ofPattern("dd MMM"))
    }
}
