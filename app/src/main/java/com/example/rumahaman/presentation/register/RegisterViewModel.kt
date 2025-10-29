package com.example.rumahaman.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rumahaman.data.repository.Result
import com.example.rumahaman.domain.usecase.register.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
// --- IMPOR TAMBAHAN ---
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
// --------------------
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    var registerState by mutableStateOf(RegisterState())
        private set

    // --- PENAMBAHAN NAVIGATION CHANNEL ---
    // 1. Channel untuk mengirim event satu kali (one-time event)
    private val _navigationChannel = Channel<NavigationEvent>()
    val navigationChannel = _navigationChannel.receiveAsFlow()
    // -------------------------------------

    fun onRegisterClick(email: String, pass: String, confirmPass: String) {
        // Validasi dasar
        if (email.isBlank() || pass.isBlank()) {
            registerState = registerState.copy(error = "Email dan kata sandi tidak boleh kosong.")
            return
        }
        if (pass != confirmPass) {
            registerState = registerState.copy(error = "Kata sandi dan konfirmasi kata sandi tidak cocok!")
            return
        }

        viewModelScope.launch {
            registerUseCase(email, pass).collect { result ->
                registerState = when (result) {
                    is Result.Loading -> {
                        registerState.copy(isLoading = true, error = null)
                    }
                    is Result.Success -> {
                        // 2. Saat registrasi sukses, kirim event navigasi melalui Channel
                        _navigationChannel.send(NavigationEvent.NavigateToLogin)
                        // Kita tetap set state sukses jika ingin menampilkan Toast atau pesan lain
                        registerState.copy(isLoading = false, isSuccess = true)
                    }
                    is Result.Error -> {
                        registerState.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Terjadi kesalahan yang tidak diketahui"
                        )
                    }
                }
            }
        }
    }

    // 3. Sealed class untuk mendefinisikan semua event navigasi yang mungkin
    sealed class NavigationEvent {
        data object NavigateToLogin : NavigationEvent()
        // Anda bisa menambahkan event lain di sini untuk navigasi ke halaman lain
    }
}

data class RegisterState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
