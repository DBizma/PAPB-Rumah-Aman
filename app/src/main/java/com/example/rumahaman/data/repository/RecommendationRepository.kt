package com.example.rumahaman.data.repository

import com.example.rumahaman.data.model.RecommendationResult
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecommendationRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val recommendationsCollection = firestore.collection("recommendations")

    suspend fun getRecommendation(
        gender: String,
        violence: String,
        need: String,
        province: String
    ): kotlin.Result<RecommendationResult> {
        return try {
            val query = recommendationsCollection
                .whereEqualTo("input.gender", gender)
                .whereEqualTo("input.violence", violence)
                .whereEqualTo("input.need", need)
                .whereEqualTo("input.province", province)
                .limit(1)
                .get()
                .await()

            if (query.documents.isNotEmpty()) {
                val recommendation = query.documents.first().toObject(RecommendationResult::class.java)
                if (recommendation != null) {
                    kotlin.Result.success(recommendation)
                } else {
                    kotlin.Result.failure(Exception("Failed to parse recommendation"))
                }
            } else {
                kotlin.Result.failure(Exception("Tidak ada rekomendasi yang sesuai"))
            }
        } catch (e: Exception) {
            kotlin.Result.failure(e)
        }
    }

    suspend fun getAllRecommendations(): kotlin.Result<List<RecommendationResult>> {
        return try {
            val query = recommendationsCollection.get().await()
            val recommendations = query.documents.mapNotNull { 
                it.toObject(RecommendationResult::class.java) 
            }
            kotlin.Result.success(recommendations)
        } catch (e: Exception) {
            kotlin.Result.failure(e)
        }
    }
}
