package com.example.rumahaman.presentation.editPassword

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rumahaman.presentation.ui.TextFieldAuth
import com.example.rumahaman.presentation.ui.theme.LightGreenGray
import com.example.rumahaman.presentation.ui.theme.RumahAmanTheme
import com.example.rumahaman.presentation.ui.theme.TealColor
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPasswordScreen(
    navController: NavController,
    viewModel: EditPasswordViewModel = hiltViewModel()
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
                title = { Text("Ubah Password", fontWeight = FontWeight.Bold) },
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
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(color = TealColor)
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                        .align(Alignment.TopCenter),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = LightGreenGray
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Lock Icon
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE9A899))
                                .border(2.dp, Color(0xFFE9A899), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password Icon",
                                tint = Color.White,
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Password Lama
                        PasswordField(
                            label = "Password Lama",
                            value = uiState.oldPassword,
                            onValueChange = { viewModel.updateOldPassword(it) },
                            showPasswordToggle = true
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Password Baru
                        PasswordField(
                            label = "Password Baru",
                            value = uiState.newPassword,
                            onValueChange = { viewModel.updateNewPassword(it) },
                            showPasswordToggle = true,
                            isValid = if (uiState.newPassword.isNotBlank()) {
                                uiState.newPassword.length >= 6
                            } else null
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Konfirmasi Password
                        PasswordField(
                            label = "Konfirmasi Password",
                            value = uiState.confirmPassword,
                            onValueChange = { viewModel.updateConfirmPassword(it) },
                            showPasswordToggle = false,
                            isValid = if (uiState.confirmPassword.isNotBlank()) {
                                uiState.confirmPassword == uiState.newPassword
                            } else null
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Tombol Simpan
                        Button(
                            onClick = { viewModel.changePassword() },
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
                    }
                }
            }
        }
    }
}

@Composable
fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    showPasswordToggle: Boolean,
    isValid: Boolean? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextFieldAuth(
            value = value,
            onValueChange = onValueChange,
            placeholder = "Masukkan $label",
            isValid = isValid,
            isPassword = true,
            showPasswordToggle = showPasswordToggle,
            backgroundColor = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditPasswordScreenPreview() {
    RumahAmanTheme {
        EditPasswordScreen(navController = rememberNavController())
    }
}


