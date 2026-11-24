package com.example.campusconnect.presentation.home.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.campusconnect.ui.theme.CampusColors

private data class CampusEvent(
    val id: Int,
    val day: String,
    val month: String,
    val category: String,
    val title: String,
    val time: String,
    val location: String
)

private val dummyEvents = listOf(
    CampusEvent(
        id = 1,
        day = "26",
        month = "NOV",
        category = "WORKSHOP",
        title = "Build with AI: Student Lab",
        time = "10:00 AM - 12:00 PM",
        location = "Innovation Centre"
    ),
    CampusEvent(
        id = 2,
        day = "28",
        month = "NOV",
        category = "CULTURAL",
        title = "Campus Cultural Fest",
        time = "04:00 PM - 08:00 PM",
        location = "College Auditorium"
    ),
    CampusEvent(
        id = 3,
        day = "02",
        month = "DEC",
        category = "ACADEMIC",
        title = "Technical Quiz Competition",
        time = "11:00 AM - 01:00 PM",
        location = "Seminar Hall"
    )
)

@Composable
fun HomeComingUp(
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
                text = "Coming up",
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

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(end = 4.dp)
        ) {
            items(
                items = dummyEvents,
                key = { it.id }
            ) { event ->
                EventCard(event = event)
            }
        }
    }
}

@Composable
private fun EventCard(
    event: CampusEvent
) {
    Surface(
        modifier = Modifier.width(280.dp),
        shape = RoundedCornerShape(16.dp),
        color = CampusColors.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // Event date
                Column(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CampusColors.FieldBorder),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = event.day,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = CampusColors.Primary
                    )

                    Text(
                        text = event.month,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = CampusColors.Primary
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = event.category,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = CampusColors.Primary
                    )

                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = CampusColors.TextPrimary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            HorizontalDivider(
                color = CampusColors.FieldBorder
            )

            // Event time
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = CampusColors.TextSecondary,
                    modifier = Modifier.size(17.dp)
                )

                Text(
                    text = event.time,
                    style = MaterialTheme.typography.bodySmall,
                    color = CampusColors.TextSecondary
                )
            }

            // Event location
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = CampusColors.TextSecondary,
                    modifier = Modifier.size(17.dp)
                )

                Text(
                    text = event.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = CampusColors.TextSecondary
                )
            }
        }
    }
}
