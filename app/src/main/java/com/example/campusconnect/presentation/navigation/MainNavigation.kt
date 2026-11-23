package com.example.campusconnect.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.campusconnect.presentation.home.HomeScreen
import com.example.campusconnect.presentation.navigation.routes.HomeRoute

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ){
        composable<HomeRoute> {
            HomeScreen()
        }
    }
}