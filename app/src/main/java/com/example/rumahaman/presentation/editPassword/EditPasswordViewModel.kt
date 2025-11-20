package com.example.rumahaman.presentation.editPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class EditPasswordUiState(
    val oldPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class EditPasswordViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditPasswordUiState())
    val uiState: StateFlow<EditPasswordUiState> = _uiState.asStateFlow()

    fun updateOldPassword(password: String) {
        _uiState.update { it.copy(oldPassword = password) }
    }

    fun updateNewPassword(password: String) {
        _uiState.update { it.copy(newPassword = password) }
    }

    fun updateConfirmPassword(password: String) {
        _uiState.update { it.copy(confirmPassword = password) }
    }

    fun changePassword() {
        val currentState = _uiState.value

        // Validasi
        if (currentState.oldPassword.isBlank()) {
            _uiState.update { it.copy(error = "Password lama tidak boleh kosong") }
            return
        }

        if (currentState.newPassword.isBlank()) {
            _uiState.update { it.copy(error = "Password baru tidak boleh kosong") }
            return
        }

        if (currentState.newPassword.length < 6) {
            _uiState.update { it.copy(error = "Password baru minimal 6 karakter") }
            return
        }

        if (currentState.confirmPassword.isBlank()) {
            _uiState.update { it.copy(error = "Konfirmasi password tidak boleh kosong") }
            return
        }

        if (currentState.newPassword != currentState.confirmPassword) {
            _uiState.update { it.copy(error = "Password baru dan konfirmasi tidak sama") }
            return
        }

        if (currentState.oldPassword == currentState.newPassword) {
            _uiState.update { it.copy(error = "Password baru tidak boleh sama dengan password lama") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null, successMessage = null) }

            try {
                val user = auth.currentUser
                if (user == null || user.email == null) {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            error = "User tidak terautentikasi"
                        )
                    }
                    return@launch
                }

                // Re-authenticate user dengan password lama
                val credential = EmailAuthProvider.getCredential(user.email!!, currentState.oldPassword)
                user.reauthenticate(credential).await()

                // Update password
                user.updatePassword(currentState.newPassword).await()

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        successMessage = "Password berhasil diubah"
                    )
                }
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("wrong-password") == true || 
                    e.message?.contains("INVALID_LOGIN_CREDENTIALS") == true ->
                        "Password lama salah"
                    e.message?.contains("network") == true ->
                        "Tidak ada koneksi internet"
                    else -> "Gagal mengubah password: ${e.message}"
                }
                
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = errorMessage
                    )
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }
}
