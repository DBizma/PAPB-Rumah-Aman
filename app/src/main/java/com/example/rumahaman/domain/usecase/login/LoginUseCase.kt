package com.example.rumahaman.domain.usecase.login

import com.example.rumahaman.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    /**
     * Operator 'invoke' memungkinkan kita memanggil kelas ini seolah-olah itu adalah sebuah fungsi.
     * Cukup panggil instance dari kelas ini dengan parameter yang dibutuhkan.
     */
    suspend operator fun invoke(email: String, password: String) = repository.loginUser(email, password)
}
