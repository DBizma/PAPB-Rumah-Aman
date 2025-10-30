package com.example.rumahaman.presentation.KodeOTPRegister

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.google.firebase.auth.PhoneAuthOptions


// Definisikan State untuk UI
sealed class OtpUiState {
    object Idle : OtpUiState()
    object Loading : OtpUiState()
    data class Success(val message: String) : OtpUiState()
    data class Error(val message: String) : OtpUiState()
}

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow<OtpUiState>(OtpUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private var verificationId: String? = null

    private var resendingToken: PhoneAuthProvider.ForceResendingToken? = null

    // Fungsi untuk meminta kode OTP
    private fun createPhoneAuthCallbacks() = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            _uiState.value = OtpUiState.Success("Verifikasi otomatis berhasil!")
            // Jika Anda ingin langsung login setelah verifikasi otomatis, tambahkan kodenya di sini
        }

        override fun onVerificationFailed(e: FirebaseException) {
            _uiState.value = OtpUiState.Error("Gagal mengirim OTP: ${e.message}")
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // Simpan verificationId dan token untuk digunakan nanti
            this@OtpViewModel.verificationId = verificationId
            this@OtpViewModel.resendingToken = token
            _uiState.value = OtpUiState.Success("Kode OTP telah dikirim.")
        }
    }

    fun sendOtp(phoneNumber: String, activity: Activity) {
        _uiState.value = OtpUiState.Loading
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(createPhoneAuthCallbacks())
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun resendOtp(phoneNumber: String, activity: Activity) {
        _uiState.value = OtpUiState.Loading
        resendingToken?.let { token ->
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(createPhoneAuthCallbacks())
                .setForceResendingToken(token)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        } ?: run {
            // Jika karena suatu alasan token tidak ada, jalankan sendOtp biasa
            sendOtp(phoneNumber, activity)
        }
    }



    // Fungsi untuk memverifikasi kode yang dimasukkan pengguna
    fun verifyOtp(otpCode: String) {
        _uiState.value = OtpUiState.Loading
        verificationId?.let {
            val credential = PhoneAuthProvider.getCredential(it, otpCode)
            // Di sini kita tidak login, hanya verifikasi.
            // Untuk skenario registrasi, kita bisa anggap ini cukup.
            // Anda bisa menambahkan logika untuk menautkan ini ke akun email/password.
            _uiState.value = OtpUiState.Success("Nomor berhasil diverifikasi!")
        } ?: run {
            _uiState.value = OtpUiState.Error("Sesi verifikasi tidak ditemukan. Minta OTP lagi.")
        }
    }

    fun resetState() {
        _uiState.value = OtpUiState.Idle
    }
}
