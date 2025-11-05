package com.example.rumahaman.presentation.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController // <-- Import rememberNavController
import com.example.rumahaman.presentation.navigation.BottomNavigationBar
import com.example.rumahaman.presentation.navigation.MainScreenNavGraph // <-- Import MainScreenNavGraph

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() { // <-- Parameter navController sudah benar dihapus
    // 1. Buat NavController BARU, khusus untuk navigasi di dalam MainScreen.
    val mainNavController = rememberNavController()

    // 2. Panggil ViewModel untuk mendapatkan status notifikasi
    val mainViewModel: MainViewModel = hiltViewModel()
    val hasNewNotifications by mainViewModel.hasNewNotifications.collectAsState()

    Scaffold(
        // 3. BottomNavBar sekarang menggunakan 'mainNavController'
        bottomBar = {
            BottomNavigationBar(
                navController = mainNavController,
                hasNewNotifications = hasNewNotifications,
                // --- PERUBAHAN DI SINI ---
                // Hapus pemanggilan onNavBarVisible karena ViewModel sudah reaktif.
                // Cukup berikan lambda kosong.
                onNavBarVisible = { }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            // 4. Tampilkan NavGraph internal. Ini sudah benar.
            MainScreenNavGraph(navController = mainNavController)
        }
    }
}
