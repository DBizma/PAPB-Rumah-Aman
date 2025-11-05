package com.example.rumahaman.presentation.pengaturan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rumahaman.navigation.Routes
import com.example.rumahaman.presentation.ui.theme.LightGreenGray
import com.example.rumahaman.presentation.ui.theme.Orange
import com.example.rumahaman.presentation.ui.theme.RumahAmanTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PengaturanScreen(
    navController: NavController,
    viewModel: PengaturanViewModel = hiltViewModel()
) {
    val userName by viewModel.userName.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // --- PERUBAHAN DIMULAI DI SINI ---
            // Satu Card untuk menampung semuanya
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LightGreenGray),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column {
                    // 1. Profile Section
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Orange),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile Picture",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = userName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Tambahkan pemisah setelah Profile Section
                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    // 2. Menu Section "Umum"
                    Text(
                        text = "Umum",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                    MenuItem(
                        icon = Icons.Default.Edit,
                        text = "Edit Profil",
                        onClick = { /* TODO: Navigasi ke Edit Profil */ }
                    )
                    MenuItem(
                        icon = Icons.Default.Lock,
                        text = "Ubah password",
                        onClick = { /* TODO: Navigasi ke Ubah Password */ }
                    )
                    MenuItem(
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        text = "Keluar",
                        onClick = {
                            viewModel.signOut()
                            navController.navigate(Routes.LOGIN_SCREEN) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                        }
                    )

                    // Tambahkan pemisah antar seksi menu
                    Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp))

                    // 3. Menu Section "Masukan"
                    Text(
                        text = "Masukan",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                    MenuItem(
                        icon = Icons.Default.Warning,
                        text = "Laporkan",
                        onClick = { /* TODO: Navigasi ke Laporkan */ }
                    )
                    MenuItem(
                        icon = Icons.Default.Email,
                        text = "Kirim masukan",
                        onClick = { /* TODO: Navigasi ke Kirim Masukan */ }
                    )
                    // Spacer di akhir untuk memberikan padding bawah
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            // --- PERUBAHAN SELESAI DI SINI ---
        }
    }
}

@Composable
fun MenuItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Transparent), // Ubah background jadi transparan karena sudah di dalam Card putih
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Orange,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, fontSize = 16.sp, color = Color.Black)
    }
}

@Preview(showBackground = true)
@Composable
fun PengaturanScreenPreview() {
    RumahAmanTheme {
        PengaturanScreen(navController = rememberNavController())
    }
}
