package com.example.rumahaman.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.rumahaman.navigation.Routes

// Sealed class untuk mendefinisikan setiap item di Bottom Navigation Bar
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Beranda : BottomNavItem(
        route = Routes.DASHBOARD, // Gunakan konstanta dari NavGraph
        title = "Beranda",
        icon = Icons.Default.Home
    )

    object Notifikasi : BottomNavItem(
        route = Routes.NOTIFICATION_SCREEN,
        title = "Notifikasi",
        icon = Icons.Default.Notifications
    )

    object Pengaturan : BottomNavItem(
        route = Routes.SETTINGS_SCREEN,
        title = "Pengaturan",
        icon = Icons.Default.Settings
    )
}
