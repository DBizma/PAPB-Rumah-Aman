package com.example.rumahaman.domain.repository

import com.example.rumahaman.data.repository.Result
import com.example.rumahaman.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserData(userId: String): Flow<Result<User>>
    suspend fun updateUserData(user: User): Flow<Result<Unit>>
    suspend fun createUserData(user: User): Flow<Result<Unit>>
}
