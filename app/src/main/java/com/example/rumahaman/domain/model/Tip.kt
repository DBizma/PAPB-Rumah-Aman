package com.example.rumahaman.domain.model

// Ini adalah POKO (Plain Old Kotlin Object), bersih dari anotasi Firebase
data class Tip(
    val id: String = "",
    val title: String = "",
    val type: String = "",
    val link: String = ""
)