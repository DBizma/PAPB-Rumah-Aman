package com.example.rumahaman.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rumahaman.data.repository.Result
import com.example.rumahaman.domain.usecase.login.LoginUseCase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException // <-- 1. Impor Exception
import com.google.firebase.auth.FirebaseAuthInvalidUserException     // <-- 2. Impor Exception
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase // Dependensi ke LoginUseCase
) : ViewModel() {

    // ... (kode lain tidak berubah) ...
    var loginState by mutableStateOf(LoginState())
        private set

    private val _navigationChannel = Channel<NavigationEvent>()
    val navigationChannel = _navigationChannel.receiveAsFlow()


    fun onLoginClick(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            loginState = loginState.copy(error = "Email dan kata sandi tidak boleh kosong.")
            return
        }

        viewModelScope.launch {
            loginUseCase(email, pass).collect { result ->
                loginState = when (result) {
                    is Result.Loading -> {
                        loginState.copy(isLoading = true, error = null)
                    }
                    is Result.Success -> {
                        _navigationChannel.send(NavigationEvent.NavigateToHome)
                        loginState.copy(isLoading = false, isSuccess = true)
                    }
                    // --- PERBAIKAN DI BLOK ERROR DI BAWAH INI ---
                    is Result.Error -> {
                        // 3. Cek jenis error dari Firebase dan berikan pesan yang sesuai
                        val errorMessage = when (result.exception) {
                            is FirebaseAuthInvalidUserException -> "Email tidak terdaftar."
                            is FirebaseAuthInvalidCredentialsException -> "Password yang Anda masukkan salah."
                            else -> "Login gagal. Silakan coba lagi." // Pesan default
                        }
                        loginState.copy(isLoading = false, error = errorMessage)
                    }
                }
            }
        }
    }

    // ... (sisa kode tidak berubah) ...
    sealed class NavigationEvent {
        data object NavigateToHome : NavigationEvent()
    }
}

data class LoginState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
