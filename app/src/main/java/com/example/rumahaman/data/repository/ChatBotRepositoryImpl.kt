package com.example.rumahaman.data.repository

import android.util.Log
import com.example.rumahaman.data.remote.GroqApiService
import com.example.rumahaman.data.remote.GroqRequest
import com.example.rumahaman.data.remote.Message
import com.example.rumahaman.domain.repository.ChatBotRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatBotRepositoryImpl @Inject constructor(
    private val groqApiService: GroqApiService,
    private val apiKey: String
) : ChatBotRepository {
    
    override suspend fun sendMessage(message: String): Flow<Result<String>> = flow {
        emit(Result.Loading)
        try {
            Log.d("ChatBot", "Sending message: $message")
            
            // Buat request ke Groq API dengan system prompt
            val request = GroqRequest(
                model = "llama-3.1-70b-versatile", // Model Llama 3.1 yang powerful
                messages = listOf(
                    Message(
                        role = "system",
                        content = """
                            Kamu adalah chatbot konselor untuk korban kekerasan seksual bernama "Chatbot RumahAman".
                            Tugasmu adalah memberikan informasi, dukungan emosional, dan panduan awal untuk korban kekerasan seksual.
                            Bersikaplah empati, suportif, dan profesional.
                            Jangan memberikan diagnosis medis atau legal advice, tapi arahkan untuk mencari bantuan profesional jika diperlukan.
                            Jawab dengan bahasa Indonesia yang hangat dan suportif.
                        """.trimIndent()
                    ),
                    Message(
                        role = "user",
                        content = message
                    )
                ),
                temperature = 0.7,
                max_tokens = 1024
            )
            
            val response = groqApiService.chatCompletion("Bearer $apiKey", request)
            val botResponse = response.choices?.firstOrNull()?.message?.content 
                ?: "Maaf, saya tidak dapat memproses pesan Anda saat ini."
            
            Log.d("ChatBot", "Response received: $botResponse")
            emit(Result.Success(botResponse))
        } catch (e: Exception) {
            Log.e("ChatBot", "Error sending message", e)
            Log.e("ChatBot", "Error details: ${e.message}")
            emit(Result.Error(e))
        }
    }
}
