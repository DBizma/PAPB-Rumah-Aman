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
                        // Jika error karena document tidak ada, buat document baru
                        val errorMessage = result.exception.message?.lowercase() ?: ""
                        android.util.Log.d("PengaturanViewModel", "Error loading user: $errorMessage")
                        
                        if (errorMessage.contains("not found") || 
                            errorMessage.contains("tidak ditemukan") ||
                            errorMessage.contains("user tidak ditemukan") ||
                            errorMessage.contains("no document")) {
                            android.util.Log.d("PengaturanViewModel", "Creating new user document")
                            createUserDocument(
                                currentUser.uid, 
                                currentUser.email ?: "",
                                currentUser.displayName ?: "User"
                            )
                        } else {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = "Gagal memuat data: ${result.exception.message}"
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

            android.util.Log.d("PengaturanViewModel", "Saving new user: $newUser")
            
            saveUserDataUseCase(newUser, isNewUser = true).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                    is Result.Success -> {
                        android.util.Log.d("PengaturanViewModel", "User created successfully")
                        // Setelah berhasil create, set user langsung
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            user = newUser,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        android.util.Log.e("PengaturanViewModel", "Failed to create user: ${result.exception.message}")
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
    