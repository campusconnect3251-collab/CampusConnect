package com.example.campusconnect.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.campusconnect.ui.theme.CampusColors

@Composable
fun HomeScreen(
    onPeopleClick: () -> Unit
) {
    Scaffold(
        containerColor = CampusColors.Background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Campus Connect",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = CampusColors.TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Connect with your campus community.",
                color = CampusColors.TextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onPeopleClick,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CampusColors.Primary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text("Find People")
            }
        }
    }
}
