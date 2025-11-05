package com.example.rumahaman.domain.repository

import com.example.rumahaman.data.repository.Result
import kotlinx.coroutines.flow.Flow

interface ChatBotRepository {
    suspend fun sendMessage(message: String): Flow<Result<String>>
}
