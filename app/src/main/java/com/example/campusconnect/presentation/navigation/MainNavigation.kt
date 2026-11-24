package com.example.campusconnect.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.campusconnect.presentation.chat.screen.ChatScreen
import com.example.campusconnect.presentation.home.screen.HomeScreen
import com.example.campusconnect.presentation.navigation.routes.ChatRoute
import com.example.campusconnect.presentation.navigation.routes.HomeRoute
import com.example.campusconnect.presentation.navigation.routes.PeopleRoute
import com.example.campusconnect.presentation.people.screen.PeopleScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ){
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