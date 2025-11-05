package com.example.rumahaman.presentation.pengaturan

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rumahaman.R
import com.example.rumahaman.domain.model.User
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
    val uiState by viewModel.uiState.collectAsState()

    // Dialog konfirmasi logout
    if (uiState.showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideLogoutDialog() },
            title = { Text("Konfirmasi Logout") },
            text = { Text("Apakah Anda yakin ingin keluar dari akun?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.confirmLogout()
                        // Navigate ke SPLASH_SCREEN dan clear semua backstack
                        navController.navigate(Routes.SPLASH_SCREEN) {
                            popUpTo(0) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                ) {
                    Text("Ya, Keluar", color = Orange)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideLogoutDialog() }) {
                    Text("Batal")
                }
            }
        )
    }

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
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Orange)
                    }
                }
                uiState.error != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = uiState.error ?: "Terjadi kesalahan",
                            color = Color.Red,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.refreshUserData() },
                            colors = ButtonDefaults.buttonColors(containerColor = Orange)
                        ) {
                            Text("Coba Lagi")
                        }
                    }
                }
                uiState.user != null -> {
                    UserSettingsContent(
                        user = uiState.user!!,
                        onLogoutClick = { viewModel.showLogoutDialog() }
                    )
                }
            }
        }
    }
}

@Composable
fun UserSettingsContent(
    user: User,
    onLogoutClick: () -> Unit
) {
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
                Column {
                    Text(
                        text = user.name.ifEmpty { "Pengguna" },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (user.email.isNotEmpty()) {
                        Text(
                            text = user.email,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // Info tambahan user
            if (user.gender.isNotEmpty() || user.age > 0 || user.province.isNotEmpty() || user.phoneNumber.isNotEmpty()) {
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                Column(modifier = Modifier.padding(16.dp)) {
                    if (user.phoneNumber.isNotEmpty()) {
                        InfoRow(label = "Nomor Telepon", value = user.phoneNumber)
                    }
                    if (user.gender.isNotEmpty()) {
                        InfoRow(label = "Jenis Kelamin", value = user.gender)
                    }
                    if (user.age > 0) {
                        InfoRow(label = "Usia", value = "${user.age} tahun")
                    }
                    if (user.province.isNotEmpty()) {
                        InfoRow(label = "Provinsi", value = user.province)
                    }
                }
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
                iconRes = R.drawable.profil,
                text = "Edit Profil",
                onClick = { /* TODO: Navigasi ke Edit Profil */ }
            )
            MenuItem(
                iconRes = R.drawable.password,
                text = "Ubah password",
                onClick = { /* TODO: Navigasi ke Ubah Password */ }
            )
            MenuItem(
                iconRes = R.drawable.keluar,
                text = "Keluar",
                onClick = onLogoutClick
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
                iconRes = R.drawable.lapor,
                text = "Laporkan",
                onClick = { /* TODO: Navigasi ke Laporkan */ }
            )
            MenuItem(
                iconRes = R.drawable.masukan,
                text = "Kirim masukan",
                onClick = { /* TODO: Navigasi ke Kirim Masukan */ }
            )
            // Spacer di akhir untuk memberikan padding bawah
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun MenuItem(@DrawableRes iconRes: Int, text: String, onClick: () -> Unit) {
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
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = text,
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
