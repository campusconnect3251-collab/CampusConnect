package com.example.campusconnect.presentation.chats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.campusconnect.presentation.chats.state.ChatsFilter
import com.example.campusconnect.ui.theme.CampusColors

@Composable
fun ChatsFilterRow(
    selectedFilter: ChatsFilter,
    onFilterSelected: (ChatsFilter) -> Unit
) {
    Row(
        modifier = Modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ChatsFilter.entries.forEach { filter ->

            val label = when (filter) {
                ChatsFilter.ALL -> "All"
                ChatsFilter.UNREAD -> "Unread"
                ChatsFilter.GROUPS -> "Groups"
            }

            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(text = label)
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = CampusColors.FieldBorder,
                    selectedLabelColor = CampusColors.Primary,
                    containerColor = CampusColors.White,
                    labelColor = CampusColors.TextSecondary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selectedFilter == filter,
                    borderColor = CampusColors.FieldBorder,
                    selectedBorderColor = CampusColors.FieldBorder
                )
            )
        }
    }
}