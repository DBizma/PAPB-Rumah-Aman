package com.example.rumahaman.presentation.chatbot

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.rumahaman.R
import com.example.rumahaman.domain.model.ChatMessage
import com.example.rumahaman.presentation.ui.theme.Orange
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBotScreen(
    navController: NavController,
    viewModel: ChatBotViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Auto scroll ke bawah saat ada pesan baru
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(uiState.messages.size - 1)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* Kosongkan title */ },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    // Latar belakang transparan agar menyatu
                    containerColor = Color.Transparent,
                    navigationIconContentColor = Color.Black
                )
            )
        },
        // Warna latar belakang utama sesuai gambar
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header dengan gambar chatbot
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    // Padding atas dikurangi agar lebih dekat ke TopAppBar
                    .padding(top = 0.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Ganti Box dengan Image dari drawable
                Image(
                    painter = painterResource(id = R.drawable.logo_chat), // Ganti dengan ID drawable Anda
                    contentDescription = "Chatting Bot Header",
                    modifier = Modifier.size(120.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "CHATTING BOT",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00695C) // Warna Teal gelap
                )
            }

            // Area chat dengan latar belakang berbeda
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    // Bentuk melengkung di bagian atas
                    .clip(RoundedCornerShape(topStart = 30.dp))
                    .background(Color(0xFFE0F2F1))
                    // Padding atas agar bubble tidak menempel
                    .padding(top = 24.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.messages) { message ->
                    ChatBubble(
                        message = message,
                        userName = uiState.userName
                    )
                }

                // Loading indicator
                if (uiState.isLoading) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        Color(0xFF009688), // Warna Teal untuk bot
                                        RoundedCornerShape(
                                            topStart = 20.dp,
                                            topEnd = 20.dp,
                                            bottomEnd = 20.dp,
                                            bottomStart = 4.dp
                                        )
                                    )
                                    .padding(12.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                    }
                }
            }

            // Input area
            ChatInputField(
                text = uiState.inputText,
                onTextChange = { viewModel.onInputTextChange(it) },
                onSendClick = { viewModel.sendMessage() },
                enabled = !uiState.isLoading
            )
        }
    }
}

@Composable
fun ChatBubble(
    message: ChatMessage,
    userName: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isFromUser) Alignment.End else Alignment.Start
    ) {
        // Label "Chatbot" atau nama user
        Text(
            text = if (message.isFromUser) userName else "Chatbot",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(
                start = if (message.isFromUser) 0.dp else 8.dp,
                end = if (message.isFromUser) 8.dp else 0.dp,
                bottom = 4.dp
            )
        )

        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    // Warna bubble sesuai gambar
                    color = if (message.isFromUser) Color(0xFFF4A999) else Color(0xFF009688),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = message.text,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ChatInputField(
    text: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    enabled: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp), // Gunakan ini, bukan clip()
        color = Color.White,
        shadowElevation = 8.dp // Bayangan otomatis mengikuti shape
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp), // Padding disesuaikan
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Masukkan pesan anda", color = Color.Gray) },
                shape = RoundedCornerShape(24.dp), // Bentuk field input membulat
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                enabled = enabled
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Tombol kirim tanpa background, hanya ikon
            IconButton(
                onClick = onSendClick,
                enabled = enabled && text.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Kirim",
                    modifier = Modifier.size(28.dp),
                    tint = Color(0xFF009688) // Warna teal agar senada dengan tema
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatInputFieldPreview() {
    ChatInputField(
        text = "Halo, ini pesan!",
        onTextChange = {},
        onSendClick = {},
        enabled = true
    )
}
