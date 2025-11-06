package com.example.rumahaman.data.repository

import android.util.Log
import com.example.rumahaman.domain.model.ChatMessage
import com.example.rumahaman.domain.repository.ChatHistoryRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatHistoryRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ChatHistoryRepository {

    companion object {
        private const val COLLECTION_CHAT_HISTORY = "chat_history"
        private const val TAG = "ChatHistoryRepo"
    }

    override suspend fun saveChatMessage(userId: String, message: ChatMessage) {
        try {
            firestore.collection(COLLECTION_CHAT_HISTORY)
                .document(userId)
                .collection("messages")
                .document(message.id)
                .set(
                    hashMapOf(
                        "id" to message.id,
                        "text" to message.text,
                        "isFromUser" to message.isFromUser,
                        "timestamp" to message.timestamp
                    )
                )
                .await()
            
            Log.d(TAG, "Chat message saved: ${message.id}")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving chat message", e)
            throw e
        }
    }

    override suspend fun getChatHistory(userId: String): Flow<List<ChatMessage>> = callbackFlow {
        val subscription = firestore.collection(COLLECTION_CHAT_HISTORY)
            .document(userId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error loading chat history", error)
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val messages = snapshot.documents.mapNotNull { doc ->
                        try {
                            ChatMessage(
                                id = doc.getString("id") ?: "",
                                text = doc.getString("text") ?: "",
                                isFromUser = doc.getBoolean("isFromUser") ?: false,
                                timestamp = doc.getLong("timestamp") ?: 0L
                            )
                        } catch (e: Exception) {
                            Log.e(TAG, "Error parsing chat message", e)
                            null
                        }
                    }
                    
                    Log.d(TAG, "Loaded ${messages.size} chat messages")
                    trySend(messages)
                }
            }

        awaitClose { subscription.remove() }
    }

    override suspend fun clearChatHistory(userId: String) {
        try {
            val messages = firestore.collection(COLLECTION_CHAT_HISTORY)
                .document(userId)
                .collection("messages")
                .get()
                .await()

            // Hapus semua pesan
            messages.documents.forEach { doc ->
                doc.reference.delete().await()
            }
            
            Log.d(TAG, "Chat history cleared for user: $userId")
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing chat history", e)
            throw e
        }
    }
}
