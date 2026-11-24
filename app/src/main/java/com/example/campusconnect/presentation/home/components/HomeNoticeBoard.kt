package com.example.campusconnect.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.campusconnect.ui.theme.CampusColors

private data class CampusUpdate(
    val id: Int,
    val category: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val date: String
)

private val dummyUpdates = listOf(
    CampusUpdate(
        id = 1,
        category = "STUDY MATERIAL",
        title = "DBMS Unit 4 Notes",
        description = "New PDF notes shared by the faculty.",
        icon = Icons.Default.Description,
        date = "Today"
    ),
    CampusUpdate(
        id = 2,
        category = "LOST & FOUND",
        title = "Blue water bottle",
        description = "Found near the college library.",
        icon = Icons.Default.Search,
        date = "Today"
    )
)

@Composable
fun HomeNoticeBoard(
    onSeeAllClick: () -> Unit = {}
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
                text = "Notice board",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CampusColors.TextPrimary
            )

            TextButton(
                onClick = onSeeAllClick,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "View all",
                    color = CampusColors.Primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Update cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            dummyUpdates.forEach { update ->
                UpdateCard(
                    update = update,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun UpdateCard(
    update: CampusUpdate,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = CampusColors.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            // Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = CampusColors.FieldBorder,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = update.icon,
                    contentDescription = null,
                    tint = CampusColors.Primary,
                    modifier = Modifier.size(21.dp)
                )
            }

            // Category
            Text(
                text = update.category,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = CampusColors.Primary
            )

            // Title
            Text(
                text = update.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = CampusColors.TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Description
            Text(
                text = update.description,
                style = MaterialTheme.typography.bodySmall,
                color = CampusColors.TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            HorizontalDivider(color = CampusColors.FieldBorder)

            Text(
                text = update.date,
                style = MaterialTheme.typography.labelSmall,
                color = CampusColors.TextSecondary
            )
        }
    }
}
