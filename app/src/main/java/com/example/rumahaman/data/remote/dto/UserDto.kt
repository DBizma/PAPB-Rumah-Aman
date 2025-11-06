package com.example.rumahaman.data.remote.dto

data class UserDto(
    val id: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val gender: String = "",
    val age: Int = 0,
    val province: String = ""
)
