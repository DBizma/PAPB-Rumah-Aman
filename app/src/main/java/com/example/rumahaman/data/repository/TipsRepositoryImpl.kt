package com.example.rumahaman.data.repository

import com.example.rumahaman.core.utils.Resource
import com.example.rumahaman.data.mapper.toDomain
import com.example.rumahaman.data.remote.firebase.dto.TipDto
import com.example.rumahaman.domain.model.Tip
import com.example.rumahaman.domain.repository.TipsRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TipsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : TipsRepository {

    override fun getAllTips(): Flow<Resource<List<Tip>>> = flow {
        emit(Resource.Loading())
        try {
            val snapshot = firestore.collection("tips").get().await()
            val tipsDto = snapshot.toObjects(TipDto::class.java)
            val tipsDomain = tipsDto.map { it.toDomain() }
            emit(Resource.Success(tipsDomain))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred."))
        }
    }
    
    override fun getTipsStream(): Flow<List<Tip>> = callbackFlow {
        val listener = firestore.collection("tips")
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Jika ada error (misal permission denied), kirim list kosong
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                
                if (snapshot != null) {
                    val tips = snapshot.toObjects(TipDto::class.java).map { it.toDomain() }
                    trySend(tips)
                } else {
                    trySend(emptyList())
                }
            }
        
        awaitClose { listener.remove() }
    }
}
