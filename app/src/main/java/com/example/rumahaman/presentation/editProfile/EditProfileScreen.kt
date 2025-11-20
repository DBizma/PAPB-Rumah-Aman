package com.example.rumahaman.presentation.editProfile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rumahaman.presentation.ui.theme.LightGreenGray
import com.example.rumahaman.presentation.ui.theme.RumahAmanTheme
import com.example.rumahaman.presentation.ui.theme.TealColor
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.successMessage, uiState.error) {
        if (uiState.successMessage != null) {
            snackbarHostState.showSnackbar(uiState.successMessage!!)
            delay(2000)
            viewModel.clearMessages()
            navController.popBackStack()
        }
        if (uiState.error != null) {
            snackbarHostState.showSnackbar(uiState.error!!)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Ubah Profil", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.Black
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center // Pusatkan konten Box
        ) {
            // Logika Loading: Jika isLoading true, tampilkan Indikator.
            // Jika false, tampilkan konten utama.
            if (uiState.isLoading) {
                CircularProgressIndicator(color = TealColor)
            } else {
                // Konten utama dalam Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                        .align(Alignment.TopCenter), // Ratakan Card ke atas dalam Box
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = LightGreenGray// Warna Card lebih terang
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Profile Picture
                        Box(
                            modifier = Modifier
                                .size(80.dp) // Ukuran lingkaran yang benar
                                .clip(CircleShape)
                                .background(Color(0xFFE9A899)) // Warna border dari gambar
                                .border(2.dp, Color(0xFFE9A899), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile Picture",
                                tint = Color.White,
                                modifier = Modifier.size(48.dp) // Ukuran ikon di dalam lingkaran
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Input Fields
                        ProfileTextField(
                            label = "Nama",
                            value = uiState.name,
                            onValueChange = { viewModel.updateName(it) }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        ProfileTextField(
                            label = "Nomor HP",
                            value = uiState.phoneNumber,
                            onValueChange = { viewModel.updatePhoneNumber(it) },
                            keyboardType = KeyboardType.Phone
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        ProfileTextField(
                            label = "Email",
                            value = uiState.email,
                            onValueChange = { viewModel.updateEmail(it) },
                            keyboardType = KeyboardType.Email
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Tombol Simpan
                        Button(
                            onClick = { viewModel.saveProfile() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TealColor,
                                contentColor = Color.White
                            ),
                            enabled = !uiState.isSaving
                        ) {
                            if (uiState.isSaving) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Text(
                                    text = "Simpan",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    } // Akhir dari Column dalam Card
                } // Akhir dari Card
            } // Akhir dari blok 'else'
        } // Akhir dari Box utama
    } // Akhir dari lambda Scaffold
} // Akhir dari Composable EditProfileScreen

// ... (Composable ProfileTextField dan Preview tetap sama)
@Composable
fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White, // Warna TextField disesuaikan
                focusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedTextColor = Color.Gray,
                focusedTextColor = Color.Black,
            ),
            placeholder = { Text(text = value, color = Color.Gray) }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    RumahAmanTheme {
        EditProfileScreen(navController = rememberNavController())
    }
}
