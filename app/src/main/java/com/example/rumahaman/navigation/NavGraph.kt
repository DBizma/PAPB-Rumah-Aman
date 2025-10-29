// presentation/navigation/NavGraph.kt

package com.example.rumahaman.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rumahaman.presentation.register.RegisterScreen
// import com.example.rumahaman.presentation.login.LoginScreen // Anda akan butuh ini nanti

// Definisikan rute sebagai konstanta agar tidak ada kesalahan ketik
object Routes {
    const val REGISTER_SCREEN = "register" // Nama rute bisa dibuat lebih simpel
    const val LOGIN_SCREEN = "login"
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
            // --- PERBAIKAN DI SINI ---
            // Teruskan instance navController ke RegisterScreen
            RegisterScreen(navController = navController)
        }

        // --- TAMBAHKAN INI UNTUK TUJUAN NAVIGASI DARI REGISTER ---
        // Buat composable untuk LoginScreen agar navigasi berhasil
        composable(Routes.LOGIN_SCREEN) {

            // LoginScreen(navController = navController)
        }
    }
}
