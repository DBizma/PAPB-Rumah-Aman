package com.example.rumahaman.domain.repository

import com.example.rumahaman.data.repository.Result
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

// Interface ini adalah "kontrak"
interface AuthRepository {
    // Fungsi untuk mendaftarkan pengguna dengan email dan password
    suspend fun registerUser(email: String, password: String): Flow<Result<AuthResult>>

    // Tambahkan fungsi lain nanti
    // suspend fun loginUser(email: String, password: String): Flow<Result<AuthResult>>
}
