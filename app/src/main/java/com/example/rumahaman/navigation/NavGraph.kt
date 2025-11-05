package com.example.rumahaman.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.rumahaman.presentation.halamanAwal.HalamanAwalScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rumahaman.presentation.login.LoginScreen
import com.example.rumahaman.presentation.main.MainScreen
import com.example.rumahaman.presentation.notification.NotificationScreen
import com.example.rumahaman.presentation.register.RegisterScreen
import com.example.rumahaman.presentation.splash.SplashScreen


object Routes {
    const val REGISTER_SCREEN = "register"
    const val LOGIN_SCREEN = "login"
    const val ONBOARDING = "onboarding"
    const val DASHBOARD = "dashboard"
    const val SPLASH_SCREEN = "splashscreen"
    const val NOTIFICATION_SCREEN = "notification"
    const val SETTINGS_SCREEN = "settings"
}


@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Routes.SPLASH_SCREEN
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // --- Halaman di Luar MainScreen ---
        composable(Routes.REGISTER_SCREEN) {
            RegisterScreen(navController = navController)
        }
        composable(Routes.LOGIN_SCREEN) {
            LoginScreen(navController = navController)
        }
        composable(Routes.ONBOARDING) {
            HalamanAwalScreen(navController = navController)
        }
        composable(Routes.SPLASH_SCREEN) {
            SplashScreen(navController = navController)
        }

        // --- Rute Utama ke MainScreen ---
        // Saat rute ini dipanggil, kita hanya menampilkan MainScreen.
        // MainScreen akan mengelola navigasi internalnya sendiri.
        composable(Routes.DASHBOARD) {
            MainScreen(rootNavController = navController) // <-- Pass navController utama
        }

        // Hapus rute NOTIFICATION_SCREEN dari sini karena sudah ditangani di dalam MainScreenNavGraph
    }
}
