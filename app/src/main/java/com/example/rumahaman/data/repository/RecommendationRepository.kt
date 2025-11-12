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
            // Log query parameters
            android.util.Log.d("RecommendationRepository", "Searching with:")
            android.util.Log.d("RecommendationRepository", "  gender: $gender")
            android.util.Log.d("RecommendationRepository", "  violence: $violence")
            android.util.Log.d("RecommendationRepository", "  need: $need")
            android.util.Log.d("RecommendationRepository", "  province: $province")
            
            val query = recommendationsCollection
                .whereEqualTo("input.gender", gender)
                .whereEqualTo("input.violence", violence)
                .whereEqualTo("input.need", need)
                .whereEqualTo("input.province", province)
                .limit(1)
                .get()
                .await()

            android.util.Log.d("RecommendationRepository", "Query returned ${query.documents.size} documents")

            if (query.documents.isNotEmpty()) {
                val doc = query.documents.first()
                android.util.Log.d("RecommendationRepository", "Document data: ${doc.data}")
                
                val recommendation = doc.toObject(RecommendationResult::class.java)
                if (recommendation != null) {
                    android.util.Log.d("RecommendationRepository", "Successfully parsed recommendation: ${recommendation.service.name}")
                    kotlin.Result.success(recommendation)
                } else {
                    android.util.Log.e("RecommendationRepository", "Failed to parse recommendation")
                    kotlin.Result.failure(Exception("Failed to parse recommendation"))
                }
            } else {
                android.util.Log.e("RecommendationRepository", "No matching recommendations found")
                kotlin.Result.failure(Exception("Tidak ada rekomendasi yang sesuai dengan kriteria Anda"))
            }
        } catch (e: Exception) {
            android.util.Log.e("RecommendationRepository", "Error getting recommendation", e)
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
