package com.example.rumahaman.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rumahaman.navigation.Routes
import com.example.rumahaman.presentation.dashboard.DashboardScreen
import com.example.rumahaman.presentation.notification.NotificationScreen
import com.example.rumahaman.presentation.settings.SettingsScreen

@Composable
fun MainScreenNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD
    ) {
        composable(Routes.DASHBOARD) {
            // Panggil Composable HomeScreen Anda
            DashboardScreen()
        }
        composable(Routes.NOTIFICATION_SCREEN) {
            // Panggil Composable NotificationScreen Anda
            NotificationScreen()
        }
        composable(Routes.SETTINGS_SCREEN) {
            // Panggil Composable SettingsScreen Anda
            SettingsScreen()
        }
    }
}




