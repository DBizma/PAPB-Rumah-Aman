package com.example.rumahaman.data.repository

import com.example.rumahaman.presentation.notification.NotificationItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.awaitClose // <-- IMPORT BARU
import kotlinx.coroutines.flow.Flow // <-- IMPORT BARU
import kotlinx.coroutines.flow.callbackFlow // <-- IMPORT BARU

// Data class untuk menampung data mentah dari Firestore
data class TipFirestore(
    val title: String = "",
    val description: String = "",
    val link: String = "",
    @ServerTimestamp
    val createdAt: Date? = null,
    val viewCount: Int = 0 // ← TAMBAH INI
)

@Singleton
class TipsRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    // Ubah fungsi ini agar mengembalikan Flow
    fun getTipsStream(): Flow<List<NotificationItem.Tip>> = callbackFlow {
        // Query ke Firestore
        val listener = firestore.collection("tips")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                // Jika ada error dari listener, tutup flow dengan error
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                // Jika snapshot null, jangan lakukan apa-apa
                if (snapshot == null) {
                    return@addSnapshotListener
                }

                // Ubah dokumen menjadi list NotificationItem.Tip
                val tipsList = snapshot.documents.mapNotNull { doc ->
                    val tipData = doc.toObject(TipFirestore::class.java)
                    tipData?.let {
                        NotificationItem.Tip(
                            id = doc.id,
                            title = it.title,
                            description = it.description,
                            link = it.link,
                            createdAt = it.createdAt,
                            viewCount = it.viewCount
                        )
                    }
                }

                // Kirim data terbaru ke dalam Flow
                trySend(tipsList)
            }

        // Saat Flow dibatalkan (misal: user keluar dari layar),
        // hentikan listener untuk menghemat resource.
        awaitClose { listener.remove() }
    }
    
    // Real-time Flow untuk mendapatkan 2 tips populer
    fun getPopularTipsStream(): Flow<List<NotificationItem.Tip>> = callbackFlow {
        val listener = firestore.collection("tips")
            .orderBy("viewCount", Query.Direction.DESCENDING)
            .limit(2)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e("TipsRepository", "Error listening to popular tips", error)
                    return@addSnapshotListener
                }

                if (snapshot == null) return@addSnapshotListener

                var tipsList = snapshot.documents.mapNotNull { doc ->
                    val tipData = doc.toObject(TipFirestore::class.java)
                    tipData?.let {
                        NotificationItem.Tip(
                            id = doc.id,
                            title = it.title,
                            description = it.description,
                            link = it.link,
                            createdAt = it.createdAt,
                            viewCount = it.viewCount
                        )
                    }
                }

                // Jika kurang dari 2, ambil tips terbaru untuk melengkapi
                if (tipsList.size < 2) {
                    firestore.collection("tips")
                        .orderBy("createdAt", Query.Direction.DESCENDING)
                        .limit(2)
                        .get()
                        .addOnSuccessListener { recentSnapshot ->
                            val recentTips = recentSnapshot.documents.mapNotNull { doc ->
                                val tipData = doc.toObject(TipFirestore::class.java)
                                tipData?.let {
                                    NotificationItem.Tip(
                                        id = doc.id,
                                        title = it.title,
                                        description = it.description,
                                        link = it.link,
                                        createdAt = it.createdAt,
                                        viewCount = it.viewCount
                                    )
                                }
                            }
                            
                            // Gabungkan dan remove duplicate
                            tipsList = (tipsList + recentTips)
                                .distinctBy { it.id }
                                .take(2)
                            
                            trySend(tipsList)
                        }
                } else {
                    trySend(tipsList)
                }
            }

        awaitClose { listener.remove() }
    }
    
    // Fungsi untuk increment view count
    suspend fun incrementTipViewCount(tipId: String): kotlin.Result<Unit> {
        return try {
            firestore.collection("tips")
                .document(tipId)
                .update("viewCount", com.google.firebase.firestore.FieldValue.increment(1))
                .await()
            
            android.util.Log.d("TipsRepository", "Successfully incremented viewCount for tip: $tipId")
            kotlin.Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("TipsRepository", "Error incrementing viewCount", e)
            kotlin.Result.failure(e)
        }
    }
}