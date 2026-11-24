package com.example.campusconnect.presentation.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.campusconnect.ui.theme.CampusColors
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeHeader(
    name: String,
    profileImageUrl: String?
) {
    val greeting = when (LocalTime.now().hour) {
        in 5..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        else -> "Good evening"
    }

    val currentDate = LocalDate.now().format(
        DateTimeFormatter.ofPattern("EEEE, dd MMMM")
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = currentDate.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = CampusColors.Primary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "$greeting, ${name.ifBlank { "there" }}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = CampusColors.TextPrimary
            )

            Text(
                text = "Here's what's happening on campus.",
                style = MaterialTheme.typography.bodySmall,
                color = CampusColors.TextSecondary
            )
        }

        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(CampusColors.Primary),
            contentAlignment = Alignment.Center
        ) {
            if (!profileImageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = profileImageUrl,
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = name
                        .split(" ")
                        .filter { it.isNotBlank() }
                        .take(2)
                        .mapNotNull { it.firstOrNull() }
                        .joinToString("")
                        .uppercase()
                        .ifBlank { "U" },
                    color = CampusColors.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
