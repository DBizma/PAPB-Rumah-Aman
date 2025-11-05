package com.example.rumahaman.data.mapper

import com.example.rumahaman.data.remote.dto.UserDto
import com.example.rumahaman.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = id,
        name = name,
        phoneNumber = phoneNumber,
        email = email,
        gender = gender,
        age = age,
        province = province
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        id = id,
        name = name,
        phoneNumber = phoneNumber,
        email = email,
        gender = gender,
        age = age,
        province = province
    )
}
