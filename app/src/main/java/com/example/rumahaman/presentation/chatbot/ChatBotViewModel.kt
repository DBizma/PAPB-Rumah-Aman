package com.example.rumahaman.presentation.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rumahaman.data.repository.Result
import com.example.rumahaman.domain.model.ChatMessage
import com.example.rumahaman.domain.usecase.chatbot.SendMessageUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatBotViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatBotUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Dapatkan nama user dari Firebase Auth
        val userName = auth.currentUser?.displayName ?: "User"
        _uiState.update { it.copy(userName = userName) }
        
        // Pesan sambutan dari chatbot
        addBotMessage("Halo $userName! Saya adalah chatbot konselor RumahAman. Ada yang bisa saya bantu?")
    }

    fun onInputTextChange(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun sendMessage() {
        val messageText = _uiState.value.inputText.trim()
        if (messageText.isEmpty()) return

        // Tambahkan pesan user ke chat
        val userMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = messageText,
            isFromUser = true,
            timestamp = System.currentTimeMillis()
        )

        _uiState.update {
            it.copy(
                messages = it.messages + userMessage,
                inputText = "",
                isLoading = true
            )
        }

        // Kirim ke Groq API
        viewModelScope.launch {
            sendMessageUseCase(messageText).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Result.Success -> {
                        addBotMessage(result.data)
                        _uiState.update { it.copy(isLoading = false, error = null) }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.exception.message ?: "Terjadi kesalahan"
                            )
                        }
                        // Tambahkan pesan error dari bot
                        addBotMessage("Maaf, saya tidak dapat memproses pesan Anda saat ini. Silakan coba lagi.")
                    }
                }
            }
        }
    }

    private fun addBotMessage(text: String) {
        val botMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = text,
            isFromUser = false,
            timestamp = System.currentTimeMillis()
        )

        _uiState.update {
            it.copy(messages = it.messages + botMessage)
        }
    }
    
    fun clearChat() {
        _uiState.update { it.copy(messages = emptyList()) }
        
        // Tampilkan pesan sambutan lagi
        val userName = _uiState.value.userName
        addBotMessage("Halo $userName! Saya adalah chatbot konselor RumahAman. Ada yang bisa saya bantu?")
    }
}
