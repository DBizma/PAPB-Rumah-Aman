package com.example.rumahaman.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rumahaman.R
import com.example.rumahaman.navigation.Routes
import com.example.rumahaman.presentation.ui.theme.RumahAmanTheme

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