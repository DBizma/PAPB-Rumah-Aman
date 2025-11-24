package com.example.rumahaman.presentation.lapor

import androidx.lifecycle.ViewModel

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class LaporUiState(
    val Laporan: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class LaporViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(LaporUiState())
    val uiState: StateFlow<LaporUiState> = _uiState.asStateFlow()

    fun updateLaporan(newValue: String) {
        _uiState.update { it.copy(Laporan = newValue) }
    }

    fun kirimLaporan() {
        val laporanText = uiState.value.Laporan

        if (laporanText.isBlank()) {
            _uiState.update { it.copy(error = "Laporan tidak boleh kosong") }
            return
        }

        _uiState.update { it.copy(isSaving = true) }

        val laporanData = hashMapOf(
            "uid" to auth.currentUser?.uid,
            "laporan" to laporanText,
            "timestamp" to System.currentTimeMillis()
        )

        Firebase.firestore.collection("laporan")
            .add(laporanData)
            .addOnSuccessListener {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        successMessage = "Laporan berhasil dikirim"
                    )
                }
            }
            .addOnFailureListener { e ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Gagal mengirim laporan: ${e.message}"
                    )
                }
            }
    }

    fun clearMessages() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }
}