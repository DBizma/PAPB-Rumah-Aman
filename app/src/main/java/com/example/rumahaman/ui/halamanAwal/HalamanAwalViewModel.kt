package com.example.rumahaman.ui.halamanAwal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Data class untuk merepresentasikan seluruh state dari HalamanAwalScreen.
 */
data class HalamanAwalUiState(
    val appName: String = "#RumahAman",
    val description: String = "#RumahAman hadir untuk membantu para pengguna dan korban kekerasan seksual yang tidak berani melapor dan tidak tahu apa yang harus dilakukannya setelah mengalami kekerasan seksual.",
    val isJoining: Boolean = false // Contoh state untuk menunjukkan proses loading
)

/**
 * ViewModel untuk HalamanAwalScreen.
 * Bertanggung jawab untuk menyimpan state UI dan menangani logika bisnis.
 */
class HalamanAwalViewModel : ViewModel() {

    // _uiState bersifat privat dan hanya dapat diubah di dalam ViewModel ini.
    private val _uiState = MutableStateFlow(HalamanAwalUiState())

    // uiState diekspos ke UI sebagai StateFlow yang read-only.
    val uiState: StateFlow<HalamanAwalUiState> = _uiState.asStateFlow()

    /**
     * Fungsi yang dipanggil ketika pengguna menekan tombol "Bergabung".
     * Di sini Anda bisa menambahkan logika navigasi, pemanggilan API, dll.
     */
    fun onJoinClicked() {
        // Gunakan viewModelScope untuk menjalankan coroutine yang aman untuk lifecycle.
        viewModelScope.launch {
            // Contoh: Menampilkan loading state (jika diperlukan)
            _uiState.update { currentState ->
                currentState.copy(isJoining = true)
            }

            // --- Di sinilah logika bisnis Anda akan ditempatkan ---
            // Misalnya:
            // 1. Validasi input (jika ada)
            // 2. Memanggil use case atau repository untuk menyimpan data
            // 3. Menunggu hasil dari proses asinkron
            // 4. Setelah selesai, mungkin menavigasi ke halaman lain (ini akan di-handle di UI
            //    dengan mendengarkan event dari ViewModel).

            // Untuk sekarang, kita hanya akan mengubah state kembali setelah selesai.
            _uiState.update { currentState ->
                currentState.copy(isJoining = false)
            }
        }
    }
}
