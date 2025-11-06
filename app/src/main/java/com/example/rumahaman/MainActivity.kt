package com.example.rumahaman

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.rumahaman.navigation.AppNavHost
import com.example.rumahaman.presentation.ui.theme.RumahAmanTheme // Pastikan nama tema Anda benar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Peluncur untuk meminta izin notifikasi.
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Izin diberikan oleh pengguna.
        } else {
            // Izin ditolak.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            RumahAmanTheme { // Menggunakan tema aplikasi Anda
                // Minta izin notifikasi saat Composable pertama kali dijalankan.
                RequestNotificationPermission()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 1. Buat SATU NavController sebagai sumber kebenaran navigasi.
                    val navController = rememberNavController()
                    // 2. Teruskan NavController tersebut ke NavHost utama.
                    AppNavHost(navController = navController)
                }
            }
        }
    }

    /**
     * Composable khusus untuk menangani logika permintaan izin.
     * Menggunakan LaunchedEffect agar hanya berjalan satu kali saat UI pertama kali digambar.
     */
    @Composable
    private fun RequestNotificationPermission() {
        // Hanya berlaku untuk Android 13 (TIRAMISU) ke atas.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            LaunchedEffect(Unit) {
                // Cek apakah izin SUDAH diberikan.
                val hasPermission = ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED

                // Jika belum, minta izinnya.
                if (!hasPermission) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}
