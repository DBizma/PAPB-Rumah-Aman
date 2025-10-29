package com.example.rumahaman.presentation.halamanAwal

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Data class untuk merepresentasikan seluruh state dari HalamanAwalScreen.
 */
data class HalamanAwalUiState(
    val appName: String = "#RumahAman",
    val description: String = "hadir untuk membantu para pengguna dan korban kekerasan seksual yang tidak berani melapor dan tidak tahu apa yang harus dilakukannya setelah mengalami kekerasan seksual.",
    val isJoining: String = "Bergabung"
)

/**
 * ViewModel untuk HalamanAwalScreen.
 * Bertanggung jawab untuk menyimpan state UI dan menangani logika bisnis.
 */
@HiltViewModel // 1. Tambahkan anotasi @HiltViewModel
class HalamanAwalViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(HalamanAwalUiState())

    val uiState: StateFlow<HalamanAwalUiState> = _uiState.asStateFlow()

//    fun onJoinClicked() {
//        viewModelScope.launch {
//            _uiState.update { currentState ->
//                currentState.copy(isJoining = true)
//            }
//
//            // Di sini Anda akan memanggil logika dari repository yang diinjeksi
//            // Contoh: authRepository.signInAnonymously()
//
//            _uiState.update { currentState ->
//                currentState.copy(isJoining = false)
//            }
//        }
//    }
}
