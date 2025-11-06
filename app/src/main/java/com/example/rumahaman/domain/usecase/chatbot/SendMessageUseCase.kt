package com.example.rumahaman.domain.usecase.chatbot

import com.example.rumahaman.data.repository.Result
import com.example.rumahaman.domain.repository.ChatBotRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: ChatBotRepository
) {
    suspend operator fun invoke(message: String): Flow<Result<String>> {
        return repository.sendMessage(message)
    }
}
