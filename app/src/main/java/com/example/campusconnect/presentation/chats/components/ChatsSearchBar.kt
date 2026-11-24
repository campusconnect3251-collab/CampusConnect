package com.example.campusconnect.presentation.chats.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.campusconnect.ui.theme.CampusColors

@Composable
fun ChatsSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "Search conversations",
                color = CampusColors.TextSecondary
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search",
                tint = CampusColors.TextSecondary
            )
        },
        singleLine = true,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = CampusColors.White,
            unfocusedContainerColor = CampusColors.White,
            focusedBorderColor = CampusColors.FieldBorder,
            unfocusedBorderColor = CampusColors.FieldBorder,
            focusedTextColor = CampusColors.TextPrimary,
            unfocusedTextColor = CampusColors.TextPrimary,
            cursorColor = CampusColors.Primary
        )
    )
}