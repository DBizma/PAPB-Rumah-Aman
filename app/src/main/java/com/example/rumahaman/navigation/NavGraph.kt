// presentation/navigation/NavGraph.kt

package com.example.rumahaman.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rumahaman.presentation.dashboard.Dashboard
import com.example.rumahaman.presentation.halamanAwal.HalamanAwalScreen
import com.example.rumahaman.presentation.login.LoginScreen
import com.example.rumahaman.presentation.register.RegisterScreen

object Routes {
    const val REGISTER_SCREEN = "register"
    const val LOGIN_SCREEN = "login"
    const val ONBOARDING = "onboarding"
    const val DASHBOARD = "dashboard"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.ONBOARDING
    ) {
        composable(Routes.REGISTER_SCREEN) {
            RegisterScreen(navController = navController)
        }

        composable(Routes.LOGIN_SCREEN) {

            LoginScreen(navController = navController)
        }

        composable(Routes.ONBOARDING) {
            HalamanAwalScreen(navController = navController)
        }

        composable(Routes.DASHBOARD) {
            Dashboard()
        }
    }
}
