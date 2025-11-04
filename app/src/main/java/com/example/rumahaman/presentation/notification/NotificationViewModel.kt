package com.example.rumahaman.presentation.notification

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rumahaman.data.repository.TipsRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

// Data class baru untuk menampung data yang sudah dikelompokkan
data class NotificationState(
    val groupedNotifications: Map<LocalDate, List<NotificationItem>> = emptyMap(),
    val isLoading: Boolean = true
)

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val tipsRepository: TipsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationState())
    val state = _state.asStateFlow()

    init {
        fetchAndGroupNotifications()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchAndGroupNotifications() {
        viewModelScope.launch {
            _state.value = NotificationState(isLoading = true)

            // 1. Ambil semua notifikasi Tips
            val tipNotifications = tipsRepository.getAllTips()

            // 2. Buat notifikasi Selamat Datang (jika relevan)
            val welcomeNotifications = createWelcomeNotifications()

            // 3. Gabungkan semua notifikasi ke dalam satu list
            val allNotifications = (tipNotifications + welcomeNotifications).toMutableList()

            // 4. Kelompokkan notifikasi berdasarkan tanggal
            val groupedData = allNotifications.groupBy {
                // Konversi timestamp dari notifikasi (yang ada di `createdAt` dalam `Date`) ke `LocalDate`
                val timestamp = when (it) {
                    is NotificationItem.Tip -> it.createdAt?.time ?: 0L
                    is NotificationItem.Welcome, is NotificationItem.NewAccount -> {
                        // Untuk notifikasi selamat datang, kita anggap tanggalnya sama dengan tanggal pembuatan akun
                        auth.currentUser?.metadata?.creationTimestamp ?: 0L
                    }
                }
                Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate()
            }
                // Urutkan grup berdasarkan tanggal (terbaru di atas)
                .toSortedMap(compareByDescending { it })


            _state.value = NotificationState(
                groupedNotifications = groupedData,
                isLoading = false
            )
        }
    }

    private fun createWelcomeNotifications(): List<NotificationItem> {
        val creationTimestamp = auth.currentUser?.metadata?.creationTimestamp ?: return emptyList()
        val creationInstant = Instant.ofEpochMilli(creationTimestamp)
        val currentInstant = Instant.now()
        val accountAgeInDays = ChronoUnit.DAYS.between(creationInstant, currentInstant)

        // Hanya tampilkan notifikasi ini jika akun dibuat dalam 7 hari terakhir
        return if (accountAgeInDays <= 7) {
            val userName = auth.currentUser?.displayName ?: "Pengguna Baru"
            listOf(
                // Kita urutkan agar 'Welcome' di atas 'NewAccount' jika tanggalnya sama
                NotificationItem.Welcome("welcome1", userName),
                NotificationItem.NewAccount("newacc1")
            )
        } else {
            emptyList()
        }
    }
}
