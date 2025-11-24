package com.example.rumahaman.presentation.masukkan

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


data class MasukkanUiState(
    val Masukkan: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class MasukkanViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _uiState = MutableStateFlow(MasukkanUiState())
    val uiState: StateFlow<MasukkanUiState> = _uiState.asStateFlow()

    fun updateMasukkan(newValue: String) {
        _uiState.update { it.copy(Masukkan = newValue) }
    }

    fun kirimMasukkan() {
        val MasukkanText = uiState.value.Masukkan

        if (MasukkanText.isBlank()) {
            _uiState.update { it.copy(error = "Masukkan tidak boleh kosong") }
            return
        }

        _uiState.update { it.copy(isSaving = true) }

        val MasukkanData = hashMapOf(
            "uid" to auth.currentUser?.uid,
            "Masukkan" to MasukkanText,
            "timestamp" to System.currentTimeMillis()
        )

        Firebase.firestore.collection("Masukkan")
            .add(MasukkanData)
            .addOnSuccessListener {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        successMessage = "Masukkan berhasil dikirim"
                    )
                }
            }
            .addOnFailureListener { e ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Gagal mengirim Masukkan: ${e.message}"
                    )
                }
            }
    }

    fun clearMessages() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }
}