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
    // --- TAMBAHKAN FIELD INI ---
    @ServerTimestamp // Anotasi ini penting untuk handle timestamp dari server
    val createdAt: Date? = null
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
                            createdAt = it.createdAt
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
}
