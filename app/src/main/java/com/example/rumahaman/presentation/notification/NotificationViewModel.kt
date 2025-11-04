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

// Data class State (tidak ada perubahan)
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
        listenForNotifications()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun listenForNotifications() {
        viewModelScope.launch {
            _state.value = NotificationState(isLoading = true)

            // Dapatkan tanggal pembuatan akun sebagai LocalDate SEKALI SAJA
            val accountCreationTimestamp = auth.currentUser?.metadata?.creationTimestamp ?: 0L
            val accountCreationDate = Instant.ofEpochMilli(accountCreationTimestamp)
                .atZone(ZoneId.systemDefault()).toLocalDate()

            tipsRepository.getTipsStream().collect { tipNotifications ->
                val welcomeNotifications = createWelcomeNotifications(accountCreationTimestamp)
                val allNotifications = tipNotifications + welcomeNotifications

                // --- LOGIKA FILTER BARU ---
                // Filter notifikasi agar hanya menampilkan yang tanggalnya SAMA DENGAN atau SETELAH tanggal pembuatan akun.
                val filteredNotifications = allNotifications.filter { item ->
                    val itemDate = getItemDate(item, accountCreationTimestamp)
                    // !isBefore sama dengan isAfter atau isEqual
                    !itemDate.isBefore(accountCreationDate)
                }
                // --- AKHIR LOGIKA FILTER ---

                val groupedData = filteredNotifications.groupBy { item ->
                    // Gunakan fungsi helper yang sama untuk konsistensi
                    getItemDate(item, accountCreationTimestamp)
                }.toSortedMap(compareByDescending { it })

                _state.value = NotificationState(
                    groupedNotifications = groupedData,
                    isLoading = false
                )
            }
        }
    }

    // Fungsi helper untuk mendapatkan tanggal dari item notifikasi secara konsisten
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getItemDate(item: NotificationItem, defaultTimestamp: Long): LocalDate {
        val timestamp = when (item) {
            is NotificationItem.Tip -> item.createdAt?.time ?: defaultTimestamp
            is NotificationItem.Welcome, is NotificationItem.NewAccount -> defaultTimestamp
        }
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createWelcomeNotifications(creationTimestamp: Long): List<NotificationItem> {
        if (creationTimestamp == 0L) return emptyList()

        val creationInstant = Instant.ofEpochMilli(creationTimestamp)
        val currentInstant = Instant.now()
        val accountAgeInDays = ChronoUnit.DAYS.between(creationInstant, currentInstant)

        return if (accountAgeInDays <= 7) {
            val userName = auth.currentUser?.displayName ?: "Pengguna Baru"
            listOf(
                NotificationItem.Welcome("welcome1", userName),
                NotificationItem.NewAccount("newacc1")
            )
        } else {
            emptyList()
        }
    }
}
