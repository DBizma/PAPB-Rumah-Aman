package com.example.rumahaman.data.repository

import com.example.rumahaman.domain.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun registerUser(email: String, password: String): Flow<Result<AuthResult>> = callbackFlow {
        // ... kode registrasi yang sudah ada ...
        trySend(Result.Loading)
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let { trySend(Result.Success(it)) }
                } else {
                    trySend(Result.Error(task.exception ?: Exception("Registrasi gagal")))
                }
            }
        awaitClose { channel.close() }
    }

    // --- IMPLEMENTASIKAN FUNGSI BARU DI BAWAH INI ---
    override suspend fun loginUser(email: String, password: String): Flow<Result<AuthResult>> = callbackFlow {
        trySend(Result.Loading)
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Kirim hasil yang sukses jika tugas berhasil
                    task.result?.let { trySend(Result.Success(it)) }
                } else {
                    // Kirim error jika tugas gagal
                    trySend(Result.Error(task.exception ?: Exception("Login gagal")))
                }
            }
        // Pastikan channel ditutup saat tidak lagi digunakan oleh coroutine
        awaitClose { channel.close() }
    }
}


// Kita butuh sealed class untuk membungkus hasil (Loading, Success, Error)
sealed class Result<out T> {
    data object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}
