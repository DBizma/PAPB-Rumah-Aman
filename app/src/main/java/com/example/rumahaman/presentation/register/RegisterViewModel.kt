package com.example.rumahaman.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
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
    private val auth: FirebaseAuth
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
                auth.createUserWithEmailAndPassword(email, pass).await()
                auth.signOut()

                _navigationChannel.send(Unit)
                _registerState.value = RegisterState(isLoading = false)

            } catch (e: Exception) {
                _registerState.value = RegisterState(error = e.message ?: "Terjadi kesalahan")
            }
        }

    }
}
