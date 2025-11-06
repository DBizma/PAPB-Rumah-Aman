package com.example.rumahaman.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rumahaman.navigation.Routes
import com.example.rumahaman.presentation.dashboard.DashboardScreen
import com.example.rumahaman.presentation.notification.NotificationScreen
import com.example.rumahaman.presentation.pengaturan.PengaturanScreen

@Composable
fun MainScreenNavGraph(
    navController: NavHostController,
    rootNavController: NavHostController // <-- Tambahkan parameter untuk root NavController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD // Halaman awal saat MainScreen terbuka adalah Beranda
    ) {
        composable(Routes.DASHBOARD) {
            DashboardScreen(navController = rootNavController)
        }
        composable(Routes.NOTIFICATION_SCREEN) {
            // Di sini kita TIDAK perlu meneruskan navController lagi ke NotificationScreen
            // karena NotificationScreen tidak memiliki navigasi internal lebih lanjut.
            NotificationScreen(navController = navController)
        }
        composable(Routes.SETTINGS_SCREEN) {
            // --- PERUBAHAN DI SINI ---
            // Pass rootNavController agar PengaturanScreen bisa navigate ke SPLASH
            PengaturanScreen(navController = rootNavController)
        }
    }
}




