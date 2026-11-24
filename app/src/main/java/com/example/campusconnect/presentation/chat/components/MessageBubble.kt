package com.example.campusconnect.presentation.chat.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.campusconnect.domain.model.Message
import com.example.campusconnect.ui.theme.CampusColors
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MessageBubble(
    message: Message,
    isMine: Boolean
) {
    val bubbleColor = if (isMine) {
        CampusColors.Primary
    } else {
        CampusColors.White
    }

    val textColor = if (isMine) {
        CampusColors.White
    } else {
        CampusColors.TextPrimary
    }

    val timeFormatter = remember {
        DateTimeFormatter.ofPattern("hh:mm a")
            .withZone(ZoneId.systemDefault())
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) {
            Arrangement.End
        } else {
            Arrangement.Start
        }
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isMine) 16.dp else 4.dp,
                        bottomEnd = if (isMine) 4.dp else 16.dp
                    )
                )
                .background(bubbleColor)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = message.text,
                color = textColor
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = timeFormatter.format(message.sentAt),
                color = if (isMine) {
                    CampusColors.White.copy(alpha = 0.75f)
                } else {
                    CampusColors.TextSecondary
                },
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
