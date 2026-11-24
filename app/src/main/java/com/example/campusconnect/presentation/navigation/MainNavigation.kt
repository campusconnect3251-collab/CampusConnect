package com.example.campusconnect.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.campusconnect.presentation.chat.screen.ChatScreen
import com.example.campusconnect.presentation.chats.screen.ChatsScreen
import com.example.campusconnect.presentation.home.screen.HomeScreen
import com.example.campusconnect.presentation.navigation.components.MainBottomNavigation
import com.example.campusconnect.presentation.navigation.components.MainTab
import com.example.campusconnect.presentation.navigation.routes.ChatRoute
import com.example.campusconnect.presentation.navigation.routes.ChatsRoute
import com.example.campusconnect.presentation.navigation.routes.HomeRoute
import com.example.campusconnect.presentation.navigation.routes.PeopleRoute
import com.example.campusconnect.presentation.people.screen.PeopleScreen
import com.example.campusconnect.ui.theme.CampusColors

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation() {

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val selectedTab = when {
        currentDestination?.hasRoute<HomeRoute>() == true ->
            MainTab.HOME

        currentDestination?.hasRoute<ChatsRoute>() == true ->
            MainTab.CHATS

        else -> null
    }

    Scaffold(
        containerColor = CampusColors.Background,
        bottomBar = {
            selectedTab?.let { tab ->
                MainBottomNavigation(
                    selectedTab = tab,
                    onTabSelected = { selectedTab ->

                        when (selectedTab) {
                            MainTab.HOME -> {
                                navController.navigate(HomeRoute) {
                                    popUpTo<HomeRoute> {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }

                            MainTab.CHATS -> {
                                navController.navigate(ChatsRoute) {
                                    popUpTo<HomeRoute> {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }

                            // These destinations will be implemented later.
                            MainTab.CAMPUS,
                            MainTab.COMMUNITIES,
                            MainTab.PROFILE -> Unit
                        }
                    }
                )
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            composable<HomeRoute> {
                HomeScreen(
                    onPeopleClick = {
                        navController.navigate(PeopleRoute) {
                            launchSingleTop = true
                        }
                    },
                    onChatSelected = { chatId, otherUserId ->
                        navController.navigate(
                            ChatRoute(
                                chatId = chatId,
                                otherUserId = otherUserId
                            )
                        )
                    }
                )
            }

            composable<ChatsRoute> {
                ChatsScreen(
                    onNewChatClick = {
                        navController.navigate(PeopleRoute) {
                            launchSingleTop = true
                        }
                    },
                    onChatClick = { chatId, otherUserId ->
                        navController.navigate(
                            ChatRoute(
                                chatId = chatId,
                                otherUserId = otherUserId
                            )
                        )
                    }
                )
            }

            composable<PeopleRoute> {
                PeopleScreen(
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onChatSelected = { chatId, otherUserId ->
                        navController.navigate(
                            ChatRoute(
                                chatId = chatId,
                                otherUserId = otherUserId
                            )
                        )
                    }
                )
            }

            composable<ChatRoute> { backStackEntry ->

                val route = backStackEntry.toRoute<ChatRoute>()

                ChatScreen(
                    chatId = route.chatId,
                    otherUserId = route.otherUserId,
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}