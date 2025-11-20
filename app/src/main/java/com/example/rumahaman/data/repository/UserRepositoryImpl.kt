package com.example.rumahaman.data.repository

import com.example.rumahaman.data.mapper.toDomain
import com.example.rumahaman.data.mapper.toDto
import com.example.rumahaman.data.remote.dto.UserDto
import com.example.rumahaman.domain.model.User
import com.example.rumahaman.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun getUserData(userId: String): Flow<Result<User>> = callbackFlow {
        trySend(Result.Loading)
        
        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    try {
                        val userDto = document.toObject(UserDto::class.java)
                        if (userDto != null) {
                            trySend(Result.Success(userDto.toDomain()))
                        } else {
                            trySend(Result.Error(Exception("Data user tidak valid")))
                        }
                    } catch (e: Exception) {
                        trySend(Result.Error(e))
                    }
                } else {
                    trySend(Result.Error(Exception("User tidak ditemukan")))
                }
            }
            .addOnFailureListener { exception ->
                trySend(Result.Error(exception))
            }
        
        awaitClose { channel.close() }
    }

    override suspend fun updateUserData(user: User): Flow<Result<Unit>> = callbackFlow {
        trySend(Result.Loading)
        
        val userDto = user.toDto()
        val updates = hashMapOf<String, Any>(
            "name" to userDto.name,
            "phoneNumber" to userDto.phoneNumber,
            "email" to userDto.email
        )
        
        firestore.collection("users")
            .document(user.id)
            .update(updates)
            .addOnSuccessListener {
                trySend(Result.Success(Unit))
            }
            .addOnFailureListener { exception ->
                trySend(Result.Error(exception))
            }
        
        awaitClose { channel.close() }
    }
    
    override suspend fun createUserData(user: User): Flow<Result<Unit>> = callbackFlow {
        trySend(Result.Loading)
        
        firestore.collection("users")
            .document(user.id)
            .set(user.toDto())
            .addOnSuccessListener {
                trySend(Result.Success(Unit))
            }
            .addOnFailureListener { exception ->
                trySend(Result.Error(exception))
            }
        
        awaitClose { channel.close() }
    }
}
