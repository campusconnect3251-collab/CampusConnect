package com.example.campusconnect.presentation.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.campusconnect.ui.theme.CampusColors

@Composable
fun MessageInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    isSending: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CampusColors.White)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(
                    text = "Type a message...",
                    color = CampusColors.TextSecondary
                )
            },
            shape = RoundedCornerShape(24.dp),
            maxLines = 4,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (!isSending && value.isNotBlank()) {
                        onSendClick()
                    }
                }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CampusColors.Primary,
                unfocusedBorderColor = CampusColors.FieldBorder,
                focusedTextColor = CampusColors.TextPrimary,
                unfocusedTextColor = CampusColors.TextPrimary,
                cursorColor = CampusColors.Primary,
                focusedContainerColor = CampusColors.White,
                unfocusedContainerColor = CampusColors.White
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = onSendClick,
            enabled = value.isNotBlank() && !isSending,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    if (value.isNotBlank() && !isSending) {
                        CampusColors.Primary
                    } else {
                        CampusColors.FieldBorder
                    }
                )
        ) {
            if (isSending) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = CampusColors.White,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send message",
                    tint = CampusColors.White
                )
            }
        }
    }
}