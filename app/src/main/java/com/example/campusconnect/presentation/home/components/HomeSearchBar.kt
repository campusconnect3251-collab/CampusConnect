package com.example.campusconnect.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.campusconnect.ui.theme.CampusColors

@Composable
fun HomeSearchBar() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = CampusColors.White,
        border = ButtonDefaults.outlinedButtonBorder,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 15.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = CampusColors.TextSecondary
            )

            Text(
                text = "Search Campus Connect",
                style = MaterialTheme.typography.bodyMedium,
                color = CampusColors.TextSecondary
            )
        }
    }
}