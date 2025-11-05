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
            Log.d("ChatBot", "=== STARTING API CALL ===")
            Log.d("ChatBot", "API Key length: ${apiKey.length}")
            Log.d("ChatBot", "API Key starts with: ${apiKey.take(10)}...")
            Log.d("ChatBot", "Sending message: $message")
            
            // Validasi API key
            if (apiKey.isBlank()) {
                Log.e("ChatBot", "API Key is empty!")
                emit(Result.Error(Exception("API Key tidak ditemukan. Pastikan GROQ_API_KEY ada di local.properties")))
                return@flow
            }
            
            // Buat request ke Groq API dengan system prompt
            val request = GroqRequest(
                model = "llama-3.3-70b-versatile", // Model Llama 3.3 terbaru (replacement untuk 3.1)
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
            
            Log.d("ChatBot", "Request prepared, calling API...")
            
            val response = groqApiService.chatCompletion("Bearer $apiKey", request)
            
            Log.d("ChatBot", "Response received!")
            Log.d("ChatBot", "Choices count: ${response.choices?.size ?: 0}")
            
            val botResponse = response.choices?.firstOrNull()?.message?.content
            
            if (botResponse.isNullOrBlank()) {
                Log.e("ChatBot", "Bot response is null or empty!")
                Log.e("ChatBot", "Full response: $response")
                emit(Result.Error(Exception("Response kosong dari API")))
                return@flow
            }
            
            Log.d("ChatBot", "Response received: $botResponse")
            emit(Result.Success(botResponse))
            
        } catch (e: retrofit2.HttpException) {
            Log.e("ChatBot", "HTTP Error: ${e.code()}")
            Log.e("ChatBot", "Error body: ${e.response()?.errorBody()?.string()}")
            emit(Result.Error(Exception("HTTP Error ${e.code()}: ${e.message()}")))
        } catch (e: java.net.UnknownHostException) {
            Log.e("ChatBot", "Network error: No internet connection")
            emit(Result.Error(Exception("Tidak ada koneksi internet. Periksa koneksi Anda.")))
        } catch (e: Exception) {
            Log.e("ChatBot", "Error sending message", e)
            Log.e("ChatBot", "Error type: ${e.javaClass.simpleName}")
            Log.e("ChatBot", "Error details: ${e.message}")
            e.printStackTrace()
            emit(Result.Error(e))
        }
    }
}
