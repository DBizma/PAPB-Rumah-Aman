package com.example.rumahaman.data.repository

import com.example.rumahaman.domain.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth // Dapatkan instance FirebaseAuth dari Hilt
) : AuthRepository {

    override suspend fun registerUser(email: String, password: String): Flow<Result<AuthResult>> = callbackFlow {
        trySend(Result.Loading) // Kirim status loading
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Jika berhasil, kirim hasilnya
                    trySend(Result.Success(task.result!!))
                } else {
                    // Jika gagal, kirim exception-nya
                    trySend(Result.Error(task.exception ?: Exception("Registrasi gagal")))
                }
            }
        // Pastikan flow tetap terbuka sampai listener selesai
        awaitClose { /* bisa ditambahkan logika cleanup jika perlu */ }
    }
}

// Kita butuh sealed class untuk membungkus hasil (Loading, Success, Error)
sealed class Result<out T> {
    data object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}
