package com.example.rumahaman.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rumahaman.R // <-- Pastikan R diimpor dengan benar
import com.example.rumahaman.navigation.Routes
import com.example.rumahaman.presentation.ui.theme.RumahAmanTheme

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {

    // Ambil tujuan navigasi dari ViewModel (Logika ini tidak diubah)
    val startDestination by viewModel.startDestination

    // Lakukan navigasi ketika startDestination sudah ditentukan
    LaunchedEffect(startDestination) {
        startDestination?.let { destination ->
            navController.navigate(destination) {
                // Hapus SplashScreen dari backstack
                popUpTo(Routes.SPLASH_SCREEN) { inclusive = true }
            }
        }
    }

    // --- PERUBAHAN UI DI SINI ---
    // Ganti Box sederhana dengan Column untuk menyusun logo dan teks
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 1. Tampilkan Logo Anda
            Image(
                painter = painterResource(id = R.drawable.logo), // Ganti dengan ID resource logo Anda
                contentDescription = "Logo Rumah Aman",
                modifier = Modifier.size(120.dp) // Sesuaikan ukurannya jika perlu
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Tampilkan Teks #rumahaman
            Text(
                text = "#rumahaman",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
    // --- AKHIR PERUBAHAN UI ---
}

// Menambahkan Preview agar mudah melihat hasilnya
@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    RumahAmanTheme {
        // Kita tidak memerlukan NavController atau ViewModel untuk preview UI
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo Rumah Aman",
                    modifier = Modifier.size(120.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "#rumahaman",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
