package com.example.rumahaman.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge // <-- 1. IMPORT Badge & BadgedBox
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api // <-- 2. IMPORT ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect // <-- 3. IMPORT LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.rumahaman.presentation.ui.theme.LightGreenGray
import com.example.rumahaman.presentation.ui.theme.LinkColor

@OptIn(ExperimentalMaterial3Api::class) // <-- 4. TAMBAHKAN ANOTASI INI untuk BadgedBox
@Composable
fun BottomNavigationBar(
    navController: NavController,
    // --- 5. TERIMA PARAMETER BARU DARI MAINSCREEN ---
    hasNewNotifications: Boolean,
    onNavBarVisible: () -> Unit
) {
    val items = listOf(
        BottomNavItem.Beranda,
        BottomNavItem.Notifikasi,
        BottomNavItem.Pengaturan
    )

    // --- 6. PANGGIL PENGECEKAN NOTIFIKASI SAAT NAVBAR MUNCUL ---
    LaunchedEffect(Unit) {
        onNavBarVisible()
    }

    NavigationBar(
        containerColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Garis indikator di atas
                        if (isSelected) {
                            Divider(
                                color = LinkColor,
                                thickness = 4.dp,
                                modifier = Modifier.width(64.dp)
                            )
                        } else {
                            // Spacer agar tata letak tidak bergeser
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // --- 7. INTEGRASI BADGEBOX DI SINI ---
                        // Jika item adalah Notifikasi, bungkus Ikon dengan BadgedBox
                        if (item.route == BottomNavItem.Notifikasi.route) {
                            BadgedBox(
                                badge = {
                                    // Tampilkan Badge hanya jika ada notifikasi baru
                                    if (hasNewNotifications) {
                                        Badge() // Badge lingkaran merah standar
                                    }
                                }
                            ) {
                                Icon(imageVector = item.icon, contentDescription = item.title)
                            }
                        } else {
                            // Untuk ikon lain, tampilkan seperti biasa
                            Icon(imageVector = item.icon, contentDescription = item.title)
                        }
                    }
                },
                label = { Text(text = item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = LinkColor,
                    selectedTextColor = LinkColor,
                    unselectedIconColor = LightGreenGray,
                    unselectedTextColor = LightGreenGray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

// Preview diupdate agar tidak error
@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    val navController = rememberNavController()
    // Berikan nilai default untuk parameter baru saat preview
    BottomNavigationBar(
        navController = navController,
        hasNewNotifications = true, // Tampilkan badge saat preview
        onNavBarVisible = {} // Tidak perlu aksi saat preview
    )
}
