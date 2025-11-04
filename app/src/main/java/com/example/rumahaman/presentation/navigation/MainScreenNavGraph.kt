package com.example.rumahaman.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rumahaman.navigation.Routes
import com.example.rumahaman.presentation.notification.NotificationScreen

@Composable
fun MainScreenNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD
    ) {
        composable(Routes.DASHBOARD) {
            // Ganti dengan Composable HomeScreen Anda
            Text("Ini adalah Home Screen")
        }
        composable(Routes.NOTIFICATION_SCREEN) {
            NotificationScreen(navController = navController)
        }
        composable(Routes.SETTINGS_SCREEN) {
            // Ganti dengan Composable SettingsScreen Anda
            Text("Ini adalah Settings Screen")
        }
    }
}
