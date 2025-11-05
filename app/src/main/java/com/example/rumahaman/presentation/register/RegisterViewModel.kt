package com.example.rumahaman.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rumahaman.data.repository.Result
import com.example.rumahaman.domain.model.User
import com.example.rumahaman.domain.usecase.user.SaveUserDataUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.tasks.await


// Definisikan state untuk UI, ini lebih baik daripada hanya channel
data class RegisterState(
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val saveUserDataUseCase: SaveUserDataUseCase
) : ViewModel() {

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    // Channel untuk navigasi setelah berhasil
    private val _navigationChannel = Channel<Unit>()
    val navigationChannel = _navigationChannel.receiveAsFlow()

    fun onRegisterClick(
        name: String,
        email: String,
        pass: String,
        confirmPass: String,
        isTermsChecked: Boolean
    ) {
        // Validasi input
        if (name.length < 3) {
            _registerState.value = RegisterState(error = "Nama harus lebih dari 3 karakter")
            return
        }
        if (!email.contains("@")) {
            _registerState.value = RegisterState(error = "Format email tidak valid")
            return
        }
        if (pass.length < 8) {
            _registerState.value = RegisterState(error = "Password minimal 8 karakter")
            return
        }
        if (pass != confirmPass) {
            _registerState.value = RegisterState(error = "Password tidak cocok")
            return
        }
        if (!isTermsChecked) {
            _registerState.value = RegisterState(error = "Anda harus menyetujui Ketentuan Layanan")
            return
        }

        // Jalankan proses pembuatan akun
        viewModelScope.launch {
            _registerState.value = RegisterState(isLoading = true)
            try {
                // 1. Buat user di Firebase Auth
                val authResult = auth.createUserWithEmailAndPassword(email, pass).await()
                val userId = authResult.user?.uid ?: throw Exception("User ID tidak ditemukan")
                
                // 2. Set displayName di Firebase Auth (PENTING!)
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                authResult.user?.updateProfile(profileUpdates)?.await()
                
                // 3. Simpan data user ke Firestore
                val user = User(
                    id = userId,
                    name = name,
                    email = email,
                    phoneNumber = "",
                    gender = "",
                    age = 0,
                    province = ""
                )
                
                saveUserDataUseCase(user).collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            // Tetap loading
                        }
                        is Result.Success -> {
                            // Berhasil, sign out dan navigasi ke login
                            auth.signOut()
                            _navigationChannel.send(Unit)
                            _registerState.value = RegisterState(isLoading = false)
                        }
                        is Result.Error -> {
                            // Meskipun gagal save ke Firestore, tetap anggap berhasil
                            auth.signOut()
                            _navigationChannel.send(Unit)
                            _registerState.value = RegisterState(isLoading = false)
                        }
                    }
                }

            } catch (e: Exception) {
                _registerState.value = RegisterState(error = e.message ?: "Terjadi kesalahan")
            }
        }

    }
}
