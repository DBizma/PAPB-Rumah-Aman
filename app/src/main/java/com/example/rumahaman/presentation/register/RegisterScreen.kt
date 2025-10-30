package com.example.rumahaman.presentation.register

// --- Impor Tambahan untuk Integrasi ---
import android.widget.Toast
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
// ------------------------------------

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.rumahaman.R
import com.example.rumahaman.navigation.Routes
import com.example.rumahaman.presentation.ui.Button
import com.example.rumahaman.presentation.ui.TextFieldAuth
import com.example.rumahaman.presentation.ui.theme.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    // --- Perubahan untuk Integrasi ---
    navController: NavController, // 1. Tambahkan NavController
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel() // 2. Dapatkan ViewModel dari Hilt
    // -------------------------------
) {
    // State untuk menyimpan input pengguna (UI TIDAK BERUBAH)
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(true) }

    // Logika Validasi Sederhana (UI TIDAK BERUBAH)
    val isNameValid = name.length >= 3
    val isEmailValid = email.contains("@")
    val isPasswordValid = password.length >= 8
    val doPasswordsMatch = password == confirmPassword && password.isNotEmpty()

    // --- Penambahan State dan Efek untuk Integrasi ---
    val registerState = viewModel.registerState
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.navigationChannel.collect { event ->
            when(event) {
                is RegisterViewModel.NavigationEvent.NavigateToLogin -> {
                    Toast.makeText(context, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show()
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            }
        }
    }
    // Menampilkan pesan error jika ada
    LaunchedEffect(key1 = registerState.error) {
        registerState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }
    // --------------------------------------------------

    Surface(modifier = modifier.fillMaxSize()) {
        // Box untuk menumpuk Column dengan CircularProgressIndicator
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                // Semua UI di bawah ini TIDAK SAYA UBAH sama sekali
                // Bagian Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) { //<- Ditambahkan aksi kembali
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Daftar",
                        style = MaterialTheme.typography.headlineSmall,
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

                // Bagian Form Input
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextFieldAuth(value = name, onValueChange = { name = it }, placeholder = "Nama", isValid = isNameValid)
                    TextFieldAuth(value = email, onValueChange = { email = it }, placeholder = "Email", isValid = isEmailValid)
                    TextFieldAuth(value = password, onValueChange = { password = it }, placeholder = "Kata Sandi", isValid = isPasswordValid, isPassword = true, showPasswordToggle = true)
                    TextFieldAuth(value = confirmPassword, onValueChange = { confirmPassword = it }, placeholder = "Ulangi Kata Sandi", isValid = doPasswordsMatch, isPassword = true)
                }

                // Bagian Checkbox
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
                    val annotatedText = buildAnnotatedString {
                        append("Saya setuju untuk ")
                        pushStringAnnotation(tag = "TOS", annotation = "terms_of_service_url")
                        withStyle(style = SpanStyle(color = LinkColor, fontWeight = FontWeight.SemiBold)) { append("Ketentuan Layanan") }
                        pop()
                        append(" dan ")
                        pushStringAnnotation(tag = "PRIVACY", annotation = "privacy_policy_url")
                        withStyle(style = SpanStyle(color = LinkColor, fontWeight = FontWeight.SemiBold)) { append("Kebijakan Privasi") }
                        pop()
                    }
                    ClickableText(
                        text = annotatedText,
                        style = MaterialTheme.typography.bodyMedium,
                        onClick = { offset ->
                            // ... logika klik teks ...
                        }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- Perubahan untuk Integrasi ---
                // Menggunakan AuthButton dan memanggil ViewModel
                Button(
                    text = "Daftar",
                    onClick = {
                        viewModel.onRegisterClick(
                            email = email,
                            pass = password,
                            confirmPass = confirmPassword
                        )
                    },
                    // Nonaktifkan tombol saat loading atau jika checkbox tidak dicentang
                    enabled = !registerState.isLoading && isChecked
                )
                // -------------------------------

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
                        onClick = {
                            navController.navigate(Routes.LOGIN_SCREEN) {
                                launchSingleTop = true
                                popUpTo(Routes.REGISTER_SCREEN) { inclusive = true }
                            }
                        }
                    )
                }
            }

            // --- Penambahan untuk Integrasi ---
            // Tampilkan Loading Indicator di tengah jika sedang loading
            if (registerState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            // ------------------------------------
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RumahAmanTheme {
        // Untuk preview, kita butuh NavController dummy
        RegisterScreen(navController = rememberNavController())
    }
}
