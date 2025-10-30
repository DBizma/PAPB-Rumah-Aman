package com.example.rumahaman.presentation.login

// --- Impor Tambahan untuk Integrasi ---import android.widget.Toast
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rumahaman.navigation.Routes
// ------------------------------------
import android.widget.Toast
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
fun LoginScreen(
    // --- Perubahan untuk Integrasi ---
    navController: NavController, // 1. Tambahkan NavController
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel() // 2. Dapatkan ViewModel
    // -------------------------------
) {
    // State untuk menyimpan input pengguna (nilai awal dikosongkan)
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    // var rememberMe by remember { mutableStateOf(true) } // Anda bisa menggunakannya nanti

    // --- Penambahan State dan Efek untuk Integrasi ---
    val loginState = viewModel.loginState
    val context = LocalContext.current

    // Efek untuk menangani navigasi dan pesan error
    LaunchedEffect(key1 = true) {
        viewModel.navigationChannel.collect { event ->
            when (event) {
                is LoginViewModel.NavigationEvent.NavigateToHome -> {
                    Toast.makeText(context, "Login Berhasil!", Toast.LENGTH_SHORT).show()
                    // Navigasi ke Home dan bersihkan backstack
//                    navController.navigate("home_screen") { // Ganti "home_screen" dengan rute home Anda
//                        popUpTo(Routes.LOGIN_SCREEN) { inclusive = true }
//                    }
                }
            }
        }
    }

    // Menampilkan pesan error jika ada dari ViewModel
    LaunchedEffect(key1 = loginState.error) {
        loginState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }
    // --------------------------------------------------

    Surface(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                // Semua UI di bawah ini TIDAK SAYA UBAH sama sekali
                // Bagian Header (Tombol Kembali dan Judul)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Masuk",
                        style = MaterialTheme.typography.headlineSmall,
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

                // Bagian Form Input
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextFieldAuth(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "Email",
                        isValid = null,
                        isPassword = false
                    )

                    TextFieldAuth(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "Kata Sandi",
                        isValid = null,
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

                // --- Perubahan untuk Integrasi ---
                Button(
                    text = "Masuk",
                    onClick = {
                        // Memanggil fungsi di ViewModel
                        viewModel.onLoginClick(email, password)
                    },
                    // Tombol akan nonaktif saat sedang loading
                    enabled = !loginState.isLoading
                )
                // -------------------------------

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
                        onClick = {
                            navController.navigate(Routes.REGISTER_SCREEN) {
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }

            // --- Penambahan untuk Integrasi ---
            // Tampilkan Loading Indicator di tengah jika sedang loading
            if (loginState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            // ------------------------------------
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    RumahAmanTheme {
        // Untuk Preview, kita butuh NavController dummy
        LoginScreen(navController = rememberNavController())
    }
}
