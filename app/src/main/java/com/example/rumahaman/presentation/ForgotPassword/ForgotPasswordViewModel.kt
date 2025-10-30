package com.example.rumahaman.presentation.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class ForgotPasswordState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordState())
    val uiState = _uiState.asStateFlow()

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            _uiState.value = ForgotPasswordState(isLoading = true)
            try {
                auth.sendPasswordResetEmail(email).await()
                _uiState.value = ForgotPasswordState(isSuccess = true)
            } catch (e: Exception) {
                _uiState.value = ForgotPasswordState(error = "Gagal mengirim email: ${e.message}")
            }
        }
    }
}
