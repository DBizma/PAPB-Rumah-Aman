// MainActivity.kt

package com.example.rumahaman

import android.Manifest // <-- IMPORT BARU
import android.content.pm.PackageManager // <-- IMPORT BARU
import android.os.Build // <-- IMPORT BARU
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts // <-- IMPORT BARU
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect // <-- IMPORT BARU
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext // <-- IMPORT BARU
import androidx.core.content.ContextCompat // <-- IMPORT BARU
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.rumahaman.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // --- TAMBAHKAN BLOK INI ---
    // Buat peluncur untuk meminta izin.
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Izin diberikan. Tidak perlu aksi spesifik di sini,
            // karena sistem akan mengizinkan notifikasi.
        } else {
            // Izin ditolak. Anda bisa menampilkan pesan penjelasan jika diperlukan,
            // tapi untuk sekarang kita biarkan saja.
        }
    }

    private fun askNotificationPermission() {
        // Hanya berlaku untuk Android 13 (TIRAMISU) ke atas.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Cek apakah izin SUDAH diberikan.
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                // Jika belum, minta izinnya.
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    // --- AKHIR BLOK ---

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // --- PANGGIL FUNGSI UNTUK MEMINTA IZIN ---
        askNotificationPermission()

        enableEdgeToEdge()
        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
                AppNavHost()
            }
        }
    }
}
