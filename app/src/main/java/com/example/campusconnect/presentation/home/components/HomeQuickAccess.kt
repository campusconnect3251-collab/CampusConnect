package com.example.campusconnect.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.campusconnect.ui.theme.CampusColors

private data class QuickAccessItem(
    val title: String,
    val icon: ImageVector
)

@Composable
fun HomeQuickAccess() {

    val items = listOf(
        QuickAccessItem("Notices", Icons.Default.Description),
        QuickAccessItem("Events", Icons.Default.CalendarMonth),
        QuickAccessItem("Lost & Found", Icons.Default.Search),
        QuickAccessItem("Resources", Icons.Default.Folder)
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        Text(
            text = "Quick access",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = CampusColors.TextPrimary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items.forEach { item ->

                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    color = CampusColors.White,
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 14.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(9.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .background(
                                    color = CampusColors.FieldBorder,
                                    shape = RoundedCornerShape(14.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = CampusColors.Primary,
                                modifier = Modifier.size(23.dp)
                            )
                        }

                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = CampusColors.TextPrimary,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
