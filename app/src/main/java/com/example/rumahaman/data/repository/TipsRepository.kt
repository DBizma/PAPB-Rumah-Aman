package com.example.rumahaman.data.repository

import com.example.rumahaman.presentation.notification.NotificationItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

// Data class untuk menampung data mentah dari Firestore
data class TipFirestore(
    val title: String = "",
    val description: String = "",
    val link: String = "",
    // --- TAMBAHKAN FIELD INI ---
    @ServerTimestamp // Anotasi ini penting untuk handle timestamp dari server
    val createdAt: Date? = null
)

@Singleton
class TipsRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    // Fungsi untuk mengambil tips terbaru dari Firestore
    // Nama fungsi bisa kita ubah agar lebih sesuai
    suspend fun getAllTips(): List<NotificationItem.Tip> {
        try {
            // Ambil SEMUA dokumen dari koleksi 'tips', urutkan berdasarkan yang terbaru
            val snapshot = firestore.collection("tips")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                // .limit(limit.toLong()) // <-- HAPUS ATAU KOMENTARI BARIS INI
                .get()
                .await()

            // ... di dalam fungsi getAllTips()
// ...
            return snapshot.documents.mapNotNull { doc ->
                val tipData = doc.toObject(TipFirestore::class.java)
                tipData?.let {
                    NotificationItem.Tip(
                        id = doc.id,
                        title = it.title,
                        description = it.description,
                        link = it.link,
                        // --- PASTIKAN BARIS INI ADA ---
                        createdAt = it.createdAt // Mengisi properti 'createdAt' dari data Firestore
                    )
                }
            }
// ...
        } catch (e: Exception) {
            println("Error fetching tips: ${e.message}")
            return emptyList()
        }
    }
}
