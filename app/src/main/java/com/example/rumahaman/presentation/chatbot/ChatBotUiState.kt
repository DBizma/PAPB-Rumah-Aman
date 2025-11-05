package com.example.rumahaman.presentation.chatbot

import com.example.rumahaman.domain.model.ChatMessage

data class ChatBotUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val inputText: String = "",
    val userName: String = "User"
)
