// presentation/navigation/NavGraph.kt

package com.example.rumahaman.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rumahaman.presentation.register.RegisterScreen

// Definisikan rute sebagai konstanta agar tidak ada kesalahan ketik
object Routes {
    const val REGISTER_SCREEN = "register_screen"
    // Tambahkan rute lain di sini nanti
    // const val LOGIN_SCREEN = "login_screen"
    // const val HOME_SCREEN = "home_screen"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        // Tentukan layar mana yang akan pertama kali ditampilkan
        startDestination = Routes.REGISTER_SCREEN
    ) {
        composable(Routes.REGISTER_SCREEN) {
            RegisterScreen(
                // Tambahkan aksi navigasi di sini nanti
                // onNavigateToLogin = { navController.navigate(Routes.LOGIN_SCREEN) }
            )
        }

        // composable(Routes.LOGIN_SCREEN) {
        //     LoginScreen(
        //         onNavigateToRegister = { navController.navigate(Routes.REGISTER_SCREEN) },
        //         onLoginSuccess = { navController.navigate(Routes.HOME_SCREEN) }
        //     )
        // }
    }
}
