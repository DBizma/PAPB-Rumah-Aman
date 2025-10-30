package com.example.rumahaman.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    // Daftar item navigasi kita
    val items = listOf(
        BottomNavItem.Beranda,
        BottomNavItem.Notifikasi,
        BottomNavItem.Pengaturan
    )

    NavigationBar {
        // 1. Ambil back stack entry saat ini untuk mengetahui rute yang sedang aktif
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        // 2. Loop melalui setiap item dan buat NavigationBarItem
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route, // Item terpilih jika rutenya cocok
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up ke start destination untuk menghindari penumpukan back stack
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        // Hindari membuat instance baru jika item yang sama diklik lagi
                        launchSingleTop = true
                        // Pulihkan state saat navigasi kembali ke item ini
                        restoreState = true
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) }
            )
        }
    }
}
