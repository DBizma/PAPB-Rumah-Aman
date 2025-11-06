package com.example.rumahaman.presentation.pengaturan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rumahaman.data.repository.Result
import com.example.rumahaman.domain.model.User
import com.example.rumahaman.domain.usecase.user.GetUserDataUseCase
import com.example.rumahaman.domain.usecase.user.SaveUserDataUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PengaturanUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val error: String? = null,
    val showLogoutDialog: Boolean = false
)

@HiltViewModel
class PengaturanViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val saveUserDataUseCase: SaveUserDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PengaturanUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Saat ViewModel dibuat, langsung ambil data user dari Firestore
        loadUserData()
    }

    private fun loadUserData() {
        val currentUser = auth.currentUser
        
        if (currentUser == null) {
            _uiState.value = PengaturanUiState(
                isLoading = false,
                error = "User tidak terautentikasi"
            )
            return
        }

        viewModelScope.launch {
            getUserDataUseCase(currentUser.uid).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            user = result.data,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        // Jika error karena document tidak ada atau permission denied, buat document baru
                        val errorMessage = result.exception.message?.lowercase() ?: ""
                        if (errorMessage.contains("not found") || 
                            errorMessage.contains("permission") ||
                            errorMessage.contains("tidak ditemukan")) {
                            createUserDocument(
                                currentUser.uid, 
                                currentUser.email ?: "",
                                currentUser.displayName ?: ""
                            )
                        } else {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = result.exception.message ?: "Terjadi kesalahan"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun createUserDocument(userId: String, email: String, name: String) {
        viewModelScope.launch {
            val newUser = User(
                id = userId,
                name = name,
                email = email,
                phoneNumber = "",
                gender = "",
                age = 0,
                province = ""
            )

            saveUserDataUseCase(newUser).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                    is Result.Success -> {
                        // Setelah berhasil create, load ulang data
                        loadUserData()
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Gagal membuat data user: ${result.exception.message}"
                        )
                    }
                }
            }
        }
    }

    // Fungsi untuk menampilkan dialog logout
    fun showLogoutDialog() {
        _uiState.value = _uiState.value.copy(showLogoutDialog = true)
    }

    // Fungsi untuk menyembunyikan dialog logout
    fun hideLogoutDialog() {
        _uiState.value = _uiState.value.copy(showLogoutDialog = false)
    }

    // Fungsi untuk konfirmasi logout
    fun confirmLogout() {
        hideLogoutDialog()
        auth.signOut()
    }

    // Fungsi untuk refresh data user
    fun refreshUserData() {
        loadUserData()
    }
}
    