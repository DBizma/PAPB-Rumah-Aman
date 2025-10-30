// presentation/navigation/NavGraph.kt

package com.example.rumahaman.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rumahaman.presentation.dashboard.Dashboard
import com.example.rumahaman.presentation.halamanAwal.HalamanAwalScreen
import com.example.rumahaman.presentation.KodeOTP.OtpRequestScreen
import com.example.rumahaman.presentation.KodeOTP.OtpVerifyScreen
import com.example.rumahaman.presentation.login.LoginScreen
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
    // const val HOME_SCREEN = "home_screen"
    const val OTP_REQUEST_SCREEN = "otp_request"
    const val OTP_VERIFY_SCREEN = "otp_verify"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH_SCREEN
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

        composable(Routes.SPLASH_SCREEN) {
            SplashScreen(navController = navController)
        }

        composable(Routes.DASHBOARD) {
            com.example.rumahaman.presentation.main.MainScreen()
        }
        // ---------- OTP REQUEST ----------
        composable(
            route = Routes.OTP_REQUEST_SCREEN,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it / 2 }, animationSpec = tween(500)) }
        ) {
            OtpRequestScreen(
                onGetOtpClick = {
                    navController.navigate(Routes.OTP_VERIFY_SCREEN)
                }
            )
        }
        // ---------- OTP VERIFY ----------
        composable(
            route = Routes.OTP_VERIFY_SCREEN,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it / 2 }, animationSpec = tween(500)) }
        ) {
            OtpVerifyScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
