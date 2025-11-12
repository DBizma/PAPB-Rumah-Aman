package com.example.rumahaman.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.rumahaman.presentation.halamanAwal.HalamanAwalScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// import com.example.rumahaman.presentation.admin.DataSeederScreen // REMOVED: Development only
import com.example.rumahaman.presentation.dashboard.DashboardScreen
import com.example.rumahaman.presentation.halamanAwal.HalamanAwalScreen
import com.example.rumahaman.presentation.chatbot.ChatBotScreen
import com.example.rumahaman.presentation.hasilrekomendasi.RecommendationResultScreen
import com.example.rumahaman.presentation.login.LoginScreen
import com.example.rumahaman.presentation.main.MainScreen
import com.example.rumahaman.presentation.notification.NotificationScreen
import com.example.rumahaman.presentation.recommendation.RecommendationScreen
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
    const val CHATBOT_SCREEN = "chatbot"
    const val RECOMMENDATION_SCREEN = "recommendation"
    const val RECOMMENDATION_RESULT_SCREEN = "recommendation_result"
    // const val DATA_SEEDER_SCREEN = "data_seeder" // REMOVED: Development only
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
        
        composable(Routes.CHATBOT_SCREEN) {
            ChatBotScreen(navController = navController)
        }
        
        composable(Routes.RECOMMENDATION_SCREEN) {
            val viewModel = hiltViewModel<com.example.rumahaman.presentation.recommendation.RecommendationViewModel>(it)
            RecommendationScreen(
                onBackClick = { 
                    android.util.Log.d("NavGraph", "Recommendation screen back clicked - navigating to Dashboard")
                    navController.popBackStack(Routes.DASHBOARD, inclusive = false)
                },
                onNavigateToResult = { 
                    android.util.Log.d("NavGraph", "Navigating to RECOMMENDATION_RESULT_SCREEN")
                    navController.navigate(Routes.RECOMMENDATION_RESULT_SCREEN)
                },
                viewModel = viewModel
            )
        }
        
        composable(Routes.RECOMMENDATION_RESULT_SCREEN) {
            android.util.Log.d("NavGraph", "RecommendationResultScreen composable created")
            // Get the same ViewModel instance from the parent entry
            val parentEntry = remember(it) {
                navController.getBackStackEntry(Routes.RECOMMENDATION_SCREEN)
            }
            val viewModel = hiltViewModel<com.example.rumahaman.presentation.recommendation.RecommendationViewModel>(parentEntry)
            
            RecommendationResultScreen(
                onBackClick = { 
                    android.util.Log.d("NavGraph", "Result screen back clicked - navigating to Dashboard")
                    // Jangan clear di sini, biar Dashboard yang clear saat user buka form lagi
                    navController.popBackStack(Routes.DASHBOARD, inclusive = false)
                },
                viewModel = viewModel
            )
        }
        
        // // Development only - untuk upload data awal (REMOVED)
        // composable(Routes.DATA_SEEDER_SCREEN) {
        //     DataSeederScreen()
        // }

        // --- Rute Utama ke MainScreen ---
        // Saat rute ini dipanggil, kita hanya menampilkan MainScreen.
        // MainScreen akan mengelola navigasi internalnya sendiri.
        composable(Routes.DASHBOARD) {
            MainScreen(rootNavController = navController) // <-- Pass navController utama
        }

        // Hapus rute NOTIFICATION_SCREEN dari sini karena sudah ditangani di dalam MainScreenNavGraph
    }
}
