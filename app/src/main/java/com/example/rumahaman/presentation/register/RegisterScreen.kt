package com.example.rumahaman.presentation.register

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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rumahaman.R
import com.example.rumahaman.presentation.ui.Button
import com.example.rumahaman.presentation.ui.TextFieldAuth
import com.example.rumahaman.presentation.ui.theme.*
import kotlin.Boolean


@OptIn(ExperimentalMaterial3Api::class) // Diperlukan untuk beberapa parameter TextFieldDefaults
@Composable
fun RegisterScreen(modifier: Modifier = Modifier) {
    // State untuk menyimpan input pengguna
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(true) }

    // --- Logika Validasi Sederhana ---
    // Anda bisa memindahkan logika ini ke ViewModel nanti
    val isNameValid = name.length > 3
    val isEmailValid = email.contains("@") // Validasi email yang sangat dasar
    val isPasswordValid = password.length >= 8
    val doPasswordsMatch = password == confirmPassword && password.isNotEmpty()

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
                    text = "Daftar",
                    style = MaterialTheme.typography.headlineSmall, // Gunakan style dari tema M3
                    fontWeight = FontWeight.Bold
                )
            }

            // Bagian Logo
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
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
                // Nama
                TextFieldAuth(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Nama",
                    isValid = isNameValid
                )

                // Email
                TextFieldAuth(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email",
                    isValid = isEmailValid
                )

                // Password
                TextFieldAuth(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Kata Sandi",
                    isValid = isPasswordValid,
                    isPassword = true,
                    showPasswordToggle = true
                )

                // Konfirmasi Password
                TextFieldAuth(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = "Ulangi Kata Sandi",
                    isValid = doPasswordsMatch,
                    isPassword = true
                )
            }

            // Bagian Checkbox Persetujuan
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    colors = CheckboxDefaults.colors(checkedColor = TealColor)
                )
                // Teks dengan bagian yang bisa diklik
                val annotatedText = buildAnnotatedString {
                    append("Saya setuju untuk ")
                    pushStringAnnotation(tag = "TOS", annotation = "terms_of_service_url")
                    withStyle(style = SpanStyle(color = LinkColor, fontWeight = FontWeight.SemiBold)) {
                        append("Ketentuan Layanan")
                    }
                    pop()
                    append(" dan ")
                    pushStringAnnotation(tag = "PRIVACY", annotation = "privacy_policy_url")
                    withStyle(style = SpanStyle(color = LinkColor, fontWeight = FontWeight.SemiBold)) {
                        append("Kebijakan Privasi")
                    }
                    pop()
                }
                ClickableText(
                    text = annotatedText,
                    style = MaterialTheme.typography.bodyMedium,
                    onClick = { offset ->
                        annotatedText.getStringAnnotations(tag = "TOS", start = offset, end = offset).firstOrNull()?.let {
                            // TODO: Aksi buka link Ketentuan Layanan
                        }
                        annotatedText.getStringAnnotations(tag = "PRIVACY", start = offset, end = offset).firstOrNull()?.let {
                            // TODO: Aksi buka link Kebijakan Privasi
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Tombol Daftar (Sekarang menggunakan AuthButton)
            Button(
                text = "Daftar",
                onClick = { /* TODO: Aksi untuk mendaftar */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Teks "Masuk di sini"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Sudah punya akun? ", style = MaterialTheme.typography.bodyMedium)
                ClickableText(
                    text = AnnotatedString("Masuk di sini"),
                    style = MaterialTheme.typography.bodyMedium.copy(color = LinkColor, fontWeight = FontWeight.Bold),
                    onClick = { /* TODO: Navigasi ke halaman Login */ }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RumahAmanTheme {
        RegisterScreen()
    }
}
