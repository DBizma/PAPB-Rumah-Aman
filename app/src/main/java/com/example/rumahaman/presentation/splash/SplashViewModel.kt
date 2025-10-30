package com.example.rumahaman.presentation.splash

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.rumahaman.navigation.Routes
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// Contoh SplashViewModel.kt
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth // Inject FirebaseAuth
) : ViewModel() {
    // Gunakan State untuk menampung tujuan navigasi
    val startDestination = mutableStateOf<String?>(null)

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        // Cek apakah ada pengguna yang login
        if (firebaseAuth.currentUser != null) {
            // Jika ada, arahkan ke Home
            startDestination.value = Routes.DASHBOARD
        } else {
            // Jika tidak ada, arahkan ke Login
            startDestination.value = Routes.ONBOARDING
        }
    }
}
