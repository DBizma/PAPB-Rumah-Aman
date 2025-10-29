package com.example.rumahaman.data.remote.firebase.dto

import com.google.firebase.firestore.PropertyName

// DTO khusus untuk interaksi dengan Firestore
data class TipDto(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("title") @set:PropertyName("title") var title: String = "",
    @get:PropertyName("type") @set:PropertyName("type") var type: String = "",
    @get:PropertyName("link") @set:PropertyName("link") var link: String = ""
    // Anda bisa menambahkan createdAt jika perlu
)
