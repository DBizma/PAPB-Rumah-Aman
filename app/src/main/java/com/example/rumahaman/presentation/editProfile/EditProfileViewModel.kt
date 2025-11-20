package com.example.rumahaman.presentation.editProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rumahaman.data.repository.Result
import com.example.rumahaman.domain.model.User
import com.example.rumahaman.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditProfileUiState(
    val user: User? = null,
    val name: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            _uiState.update { it.copy(error = "User tidak ditemukan") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            userRepository.getUserData(userId).collect { result ->
                when (result) {
                    is Result.Success -> {
                        val user = result.data
                        _uiState.update {
                            it.copy(
                                user = user,
                                name = user.name,
                                phoneNumber = user.phoneNumber,
                                email = user.email,
                                isLoading = false
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.exception.message ?: "Gagal memuat data"
                            )
                        }
                    }
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    fun updateName(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun updatePhoneNumber(newPhone: String) {
        _uiState.update { it.copy(phoneNumber = newPhone) }
    }

    fun updateEmail(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun saveProfile() {
        val currentState = _uiState.value
        val user = currentState.user
        
        if (user == null) {
            _uiState.update { it.copy(error = "Data user belum dimuat") }
            return
        }

        // Validasi
        if (currentState.name.isBlank()) {
            _uiState.update { it.copy(error = "Nama tidak boleh kosong") }
            return
        }

        if (currentState.phoneNumber.isBlank()) {
            _uiState.update { it.copy(error = "Nomor HP tidak boleh kosong") }
            return
        }

        if (currentState.email.isBlank()) {
            _uiState.update { it.copy(error = "Email tidak boleh kosong") }
            return
        }

        // Validasi format email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
            _uiState.update { it.copy(error = "Format email tidak valid") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null, successMessage = null) }

            val updatedUser = user.copy(
                name = currentState.name.trim(),
                phoneNumber = currentState.phoneNumber.trim(),
                email = currentState.email.trim()
            )

            try {
                userRepository.updateUserData(updatedUser).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    isSaving = false,
                                    successMessage = "Profil berhasil diperbarui",
                                    user = updatedUser
                                )
                            }
                        }
                        is Result.Error -> {
                            _uiState.update {
                                it.copy(
                                    isSaving = false,
                                    error = result.exception.message ?: "Gagal menyimpan data"
                                )
                            }
                        }
                        is Result.Loading -> {
                            _uiState.update { it.copy(isSaving = true) }
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Error: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }
}
