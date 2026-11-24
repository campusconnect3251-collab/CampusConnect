package com.example.campusconnect.presentation.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.campusconnect.ui.theme.CampusColors

enum class HomeBottomTab {
    HOME,
    CHATS,
    CAMPUS,
    COMMUNITIES,
    PROFILE
}

@Composable
fun HomeBottomNavigation(
    selectedTab: HomeBottomTab,
    onTabSelected: (HomeBottomTab) -> Unit
) {
    val tabs = listOf(
        Triple(HomeBottomTab.HOME, "Home", Icons.Outlined.Home),
        Triple(HomeBottomTab.CHATS, "Chats", Icons.Outlined.ChatBubbleOutline),
        Triple(HomeBottomTab.CAMPUS, "Campus", Icons.Outlined.School),
        Triple(HomeBottomTab.COMMUNITIES, "Communities", Icons.Outlined.Groups),
        Triple(HomeBottomTab.PROFILE, "Profile", Icons.Outlined.PersonOutline)
    )

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = CampusColors.White,
        contentColor = CampusColors.TextPrimary
    ) {
        tabs.forEach { (tab, label, icon) ->

            NavigationBarItem(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = if (selectedTab == tab) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Normal
                        }
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = CampusColors.Primary,
                    selectedTextColor = CampusColors.Primary,
                    indicatorColor = CampusColors.FieldBorder,
                    unselectedIconColor = CampusColors.TextSecondary,
                    unselectedTextColor = CampusColors.TextSecondary
                )
            )
        }
    }
}
