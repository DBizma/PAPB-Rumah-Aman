package com.example.rumahaman.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rumahaman.presentation.navigation.BottomNavigationBar
import com.example.rumahaman.presentation.navigation.MainScreenNavGraph

@Composable
fun MainScreen() {
    // Kita buat NavController baru khusus untuk bagian dalam MainScreen (Home, Profile, Settings)
    val mainNavController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = mainNavController) }
    ) { innerPadding ->
        // Gunakan Box untuk menerapkan padding dari Scaffold ke konten
        Box(modifier = Modifier.padding(innerPadding)) {
            // NavHost yang berisi layar-layar yang bisa diakses dari navbar
            MainScreenNavGraph(navController = mainNavController)
        }
    }
}
