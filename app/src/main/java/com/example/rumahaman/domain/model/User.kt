package com.example.rumahaman.domain.model

data class User(
    val id: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val gender: String = "",
    val age: Int = 0,
    val province: String = ""
)

