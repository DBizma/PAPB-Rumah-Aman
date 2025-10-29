package com.example.rumahaman.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rumahaman.R
import com.example.rumahaman.presentation.ui.Button
import com.example.rumahaman.presentation.ui.TextFieldAuth
import com.example.rumahaman.presentation.ui.theme.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    // State untuk menyimpan input pengguna
    var email by remember { mutableStateOf("Email") }
    var password by remember { mutableStateOf("**********") }
    var rememberMe by remember { mutableStateOf(true) }

    // --- Logika Validasi Sederhana ---
    // Anda bisa memindahkan logika ini ke ViewModel nanti
    val isEmailValid = email.contains("@") && email.length > 5
    val isPasswordValid = password.length >= 8

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            // Bagian Header (Tombol Kembali dan Judul)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* TODO: Aksi untuk kembali */ }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali"
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Masuk",
                    style = MaterialTheme.typography.headlineSmall, // Gunakan style dari tema M3
                    fontWeight = FontWeight.Bold
                )
            }

            // Bagian Logo
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp, top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo Rumah Aman",
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "#RumahAman",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            // Bagian Form Input (Sekarang menggunakan komponen reusable)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Email
                TextFieldAuth(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email",
                    isValid = null, // Tidak perlu validasi ikon di login
                    isPassword = false
                )

                // Password
                TextFieldAuth(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Kata Sandi",
                    isValid = null, // Tidak perlu validasi ikon di login
                    isPassword = true,
                    showPasswordToggle = true
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.End
            ) {
                ClickableText(
                    text = AnnotatedString("Lupa Password?"),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = LinkColor,
                        fontWeight = FontWeight.SemiBold
                    ),
                    onClick = { /* TODO: Navigasi ke halaman Lupa Password */ }
                )
            }


            Spacer(modifier = Modifier.height(32.dp))

            // Tombol Masuk (Sekarang menggunakan AuthButton)
            Button(
                text = "Masuk",
                onClick = { /* TODO: Aksi untuk masuk */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Teks "Daftarkan dirimu"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Belum punya akun? ", style = MaterialTheme.typography.bodyMedium)
                ClickableText(
                    text = AnnotatedString("Daftarkan dirimu"),
                    style = MaterialTheme.typography.bodyMedium.copy(color = LinkColor, fontWeight = FontWeight.Bold),
                    onClick = { /* TODO: Navigasi ke halaman Register */ }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    RumahAmanTheme {
        LoginScreen()
    }
}
