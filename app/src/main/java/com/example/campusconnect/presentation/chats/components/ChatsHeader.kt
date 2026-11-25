package com.example.campusconnect.presentation.chats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campusconnect.ui.theme.CampusColors

@Composable
fun ChatsHeader(
    onNewChatClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Messages",
            modifier = Modifier.weight(1f),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = CampusColors.TextPrimary
        )

//        Box(
//            modifier = Modifier
//                .size(44.dp)
//                .clip(RoundedCornerShape(12.dp))
//                .background(CampusColors.Primary)
//                .clickable(onClick = onNewChatClick),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                imageVector = Icons.Default.Add,
//                contentDescription = "New chat",
//                tint = CampusColors.White,
//                modifier = Modifier.size(24.dp)
//            )
//        }
    }
}