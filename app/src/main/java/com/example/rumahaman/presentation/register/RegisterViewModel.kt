package com.example.rumahaman.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {

    // Gunakan Channel untuk navigasi sekali jalan (single-shot event)
    private val _navigationChannel = Channel<Unit>()
    val navigationChannel = _navigationChannel.receiveAsFlow()

    fun onRegisterClick(
        name: String,
        email: String,
        pass: String,
        confirmPass: String,
        isTermsChecked: Boolean
    ): String? { // Return String? untuk pesan error
        // Lakukan semua validasi di sini
        if (name.length <= 3) return "Nama harus lebih dari 2 karakter"
        if (!email.contains("@")) return "Format email tidak valid"
        if (pass.length < 8) return "Password minimal 8 karakter"
        if (pass != confirmPass) return "Password tidak cocok"
        if (!isTermsChecked) return "Anda harus menyetujui Ketentuan Layanan"

        // Jika semua validasi lolos, kirim event untuk navigasi
        viewModelScope.launch {
            _navigationChannel.send(Unit)
        }

        return null // Tidak ada error
    }
}
