// presentation/navigation/NavGraph.kt

package com.example.rumahaman.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rumahaman.presentation.halamanAwal.HalamanAwalScreen
import com.example.rumahaman.presentation.KodeOTPRegister.OtpRequestScreen
import com.example.rumahaman.presentation.KodeOTPRegister.OtpUiState
import com.example.rumahaman.presentation.KodeOTPRegister.OtpVerifyScreen
import com.example.rumahaman.presentation.KodeOTPRegister.OtpViewModel
import com.example.rumahaman.presentation.forgotpassword.ForgotPasswordScreen
import com.example.rumahaman.presentation.forgotpassword.ForgotPasswordViewModel
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
    const val OTP_REQUEST_SCREEN = "otp_request"
    const val OTP_VERIFY_SCREEN = "otp_verify/{phoneNumber}"
    const val FORGOT_PASSWORD_SCREEN = "forgot_pass"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current

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
        // --- ALUR VERIFIKASI NOMOR TELEPON ---

        composable(Routes.OTP_REQUEST_SCREEN) {
            // Kita butuh OtpViewModel di sini untuk mengirim OTP
            val otpViewModel: OtpViewModel = hiltViewModel()

            OtpRequestScreen(
                onGetOtpClick = { phoneNumber ->
                    // 1. Panggil fungsi untuk mengirim OTP
                    otpViewModel.sendOtp(phoneNumber, context as android.app.Activity)
                    // 2. Navigasi ke layar verifikasi, kirim nomor telepon sebagai argumen
                    navController.navigate("otp_verify/$phoneNumber")
                }
            )
        }
        // ---------- OTP VERIFY ----------
        composable(
            route = Routes.OTP_VERIFY_SCREEN,
            arguments = listOf(navArgument("phoneNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            // Dapatkan ViewModel yang sama yang digunakan oleh OtpRequestScreen
            val otpViewModel: OtpViewModel = hiltViewModel(backStackEntry)
            val otpState by otpViewModel.uiState.collectAsState()

            // Handle navigasi setelah verifikasi berhasil
            if (otpState is OtpUiState.Success && (otpState as OtpUiState.Success).message.contains("diverifikasi")) {
                navController.navigate(Routes.LOGIN_SCREEN) {
                    popUpTo(Routes.LOGIN_SCREEN) { inclusive = true }
                }
                otpViewModel.resetState() // Reset state agar tidak trigger lagi
            }

            OtpVerifyScreen(
                phoneNumber = phoneNumber,
                onVerifyClick = { otpCode ->
                    // Aksi verifikasi tidak berubah
                    otpViewModel.verifyOtp(otpCode)
                },
                onResendOtpClick = {
                    // --- AKSI KIRIM ULANG ---
                    // Panggil fungsi resendOtp dari ViewModel
                    otpViewModel.resendOtp(phoneNumber, context as android.app.Activity)
                    // Anda bisa menambahkan Toast/Snackbar di sini untuk memberitahu pengguna
                },
                onChangePhoneClick = {
                    // --- AKSI GANTI NOMOR TELEPON ---
                    // Kembali ke layar OtpRequestScreen
                    navController.navigate(Routes.OTP_REQUEST_SCREEN) {
                        // Hapus OtpVerifyScreen dari back stack
                        popUpTo(Routes.OTP_REQUEST_SCREEN) {
                            inclusive = true
                        }
                    }
                }
            )
        }


        composable(Routes.FORGOT_PASSWORD_SCREEN) {
            val forgotPasswordViewModel: ForgotPasswordViewModel = hiltViewModel()
            val state by forgotPasswordViewModel.uiState.collectAsState()

            // Handle navigasi setelah email berhasil dikirim
            if (state.isSuccess) {
                // TODO: Tampilkan Snackbar/Toast "Link reset password telah dikirim"
                navController.navigate(Routes.LOGIN_SCREEN) {
                    popUpTo(Routes.LOGIN_SCREEN) { inclusive = true }
                }
            }

            ForgotPasswordScreen(
                onSendLinkClick = { email ->
                    forgotPasswordViewModel.sendPasswordResetEmail(email)
                },
                isLoading = state.isLoading,
                error = state.error
            )
        }
    }
}
