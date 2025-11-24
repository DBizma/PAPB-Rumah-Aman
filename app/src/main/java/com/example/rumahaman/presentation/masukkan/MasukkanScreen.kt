package com.example.rumahaman.presentation.masukkan


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rumahaman.presentation.ui.theme.LightGreenGray
import com.example.rumahaman.presentation.ui.theme.RumahAmanTheme
import com.example.rumahaman.presentation.ui.theme.TealColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MasukkanScreen(
    navController: NavController,
    viewModel: MasukkanViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            showSuccessDialog = true
        }
    }

    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            snackbarHostState.showSnackbar(uiState.error!!)
            viewModel.clearMessages()
        }
    }

    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text(
                    "Berhasil!",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Masukkan berhasil dikirim")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        viewModel.clearMessages()
                        navController.popBackStack()
                    }
                ) {
                    Text("OK", color = TealColor)
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Kirim Sugesti", fontWeight = FontWeight.Bold) },
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
//                        // Lock Icon
//                        Box(
//                            modifier = Modifier
//                                .size(80.dp)
//                                .clip(CircleShape)
//                                .background(Color(0xFFE9A899))
//                                .border(2.dp, Color(0xFFE9A899), CircleShape),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Lock,
//                                contentDescription = "Password Icon",
//                                tint = Color.White,
//                                modifier = Modifier.size(48.dp)
//                            )
//                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Laporan Field
                        MasukkanField(
                            label = "Masukkan Sugesti yang ingin kamu berikan",
                            value = uiState.Masukkan,
                            onValueChange = { viewModel.updateMasukkan(it) }
                        )


                        Spacer(modifier = Modifier.height(32.dp))

                        // Tombol Simpan
                        Button(
                            onClick = { viewModel.kirimMasukkan() },
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
fun MasukkanField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isValid: Boolean? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        // Label
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Kotak besar
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            placeholder = { Text("Tulis Masukkan...") },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedTextColor = Color.Gray,
                focusedTextColor = Color.Black,
            ),
            maxLines = 10, // biar multiline
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MasukkanScreenPreview() {
    RumahAmanTheme {
        MasukkanScreen(navController = rememberNavController())
    }
}


