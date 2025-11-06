package com.example.rumahaman.data.remote

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GroqApiService {
    @POST("openai/v1/chat/completions")
    suspend fun chatCompletion(
        @Header("Authorization") authorization: String,
        @Body request: GroqRequest
    ): GroqResponse
}

data class GroqRequest(
    val model: String,
    val messages: List<Message>,
    val temperature: Double = 0.7,
    val max_tokens: Int = 1024
)

data class Message(
    val role: String, // "system", "user", atau "assistant"
    val content: String
)

data class GroqResponse(
    val choices: List<Choice>?
)

data class Choice(
    val message: Message?
)
