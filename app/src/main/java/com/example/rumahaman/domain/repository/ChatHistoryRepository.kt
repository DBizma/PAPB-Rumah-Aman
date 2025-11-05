package com.example.rumahaman.domain.repository

import com.example.rumahaman.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatHistoryRepository {
    suspend fun saveChatMessage(userId: String, message: ChatMessage)
    suspend fun getChatHistory(userId: String): Flow<List<ChatMessage>>
    suspend fun clearChatHistory(userId: String)
}
