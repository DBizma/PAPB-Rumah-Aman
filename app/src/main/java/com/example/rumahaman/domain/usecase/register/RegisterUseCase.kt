package com.example.rumahaman.domain.usecase.register

import com.example.rumahaman.domain.repository.AuthRepository
import javax.inject.Inject

// 1. Anotasi @Inject memberitahu Hilt cara membuat instance dari kelas ini
class RegisterUseCase @Inject constructor(
    // 2. Use case ini bergantung pada AuthRepository (menggunakan interface, bukan implementasi)
    private val repository: AuthRepository
) {
    /**
     * Operator 'invoke' memungkinkan kita untuk memanggil kelas ini seolah-olah itu adalah sebuah fungsi.
     * Contoh: val useCase = RegisterUseCase(repository)
     *          useCase(email, password) // Memanggil fungsi invoke() ini
     */
    suspend operator fun invoke(email: String, password: String) = repository.registerUser(email, password)
}