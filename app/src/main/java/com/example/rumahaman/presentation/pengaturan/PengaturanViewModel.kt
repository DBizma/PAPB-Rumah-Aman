package com.example.rumahaman.presentation.pengaturan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PengaturanViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    // State untuk menampung nama pengguna saat ini
    private val _userName = MutableStateFlow("...")
    val userName = _userName.asStateFlow()

    init {
        // Saat ViewModel dibuat, langsung ambil nama pengguna
        _userName.value = auth.currentUser?.displayName ?: "Pengguna"
    }

    // Fungsi untuk keluar dari akun
    fun signOut() {
        auth.signOut()
    }
}
    