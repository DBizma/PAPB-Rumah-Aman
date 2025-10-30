package com.example.rumahaman.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider // <-- 1. IMPORT Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier // <-- 2. IMPORT Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp // <-- 3. IMPORT dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.rumahaman.presentation.ui.theme.LightGreenGray
import com.example.rumahaman.presentation.ui.theme.LinkColor

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Beranda,
        BottomNavItem.Notifikasi,
        BottomNavItem.Pengaturan
    )

    NavigationBar(
        containerColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            val isSelected = currentRoute == item.route // <-- Simpan status 'selected' dalam variabel

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
                // --- PERUBAHAN UTAMA DI SINI ---
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // 4. Buat Divider yang hanya muncul jika item terpilih
                        if (isSelected) {
                            Divider(
                                color = LinkColor, // Warna hijau dari tema Anda
                                thickness = 4.dp,
                                modifier = Modifier.width(64.dp) // Sesuaikan lebar garis
                            )
                        } else {
                            // Beri ruang kosong agar tata letak tidak bergeser
                            Spacer(modifier = Modifier.height(2.dp))
                        }

                        Spacer(modifier = Modifier.height(4.dp)) // Jarak antara garis dan ikon

                        // Ikon seperti biasa
                        Icon(imageVector = item.icon, contentDescription = item.title)
                    }
                },
                // ---------------------------------
                label = { Text(text = item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = LinkColor,
                    selectedTextColor = LinkColor,
                    unselectedIconColor = LightGreenGray,
                    unselectedTextColor = LightGreenGray
                )
            )
        }
    }
}

// Preview tidak perlu diubah
@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    val navController = rememberNavController()
    BottomNavigationBar(navController = navController)
}
