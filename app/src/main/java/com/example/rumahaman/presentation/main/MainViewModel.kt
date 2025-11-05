package com.example.rumahaman.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rumahaman.data.repository.TipsRepository
import com.example.rumahaman.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine // <-- IMPORT BARU
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tipsRepository: TipsRepository,
    private val userPrefsRepository: UserPreferencesRepository
) : ViewModel() {

    private val _hasNewNotifications = MutableStateFlow(false)
    val hasNewNotifications = _hasNewNotifications.asStateFlow()

    init {
        listenForNewNotifications()
    }

    private fun listenForNewNotifications() {
        viewModelScope.launch {
            // Gabungkan Flow tips terbaru dengan Flow timestamp terakhir dilihat
            combine(
                tipsRepository.getTipsStream(),
                userPrefsRepository.lastNotificationViewTimestampFlow
            ) { latestTips, lastViewTimestamp ->
                // Blok ini akan berjalan setiap kali 'latestTips' ATAU 'lastViewTimestamp' berubah.

                if (latestTips.isNotEmpty()) {
                    val latestTipTimestamp = latestTips.first().createdAt?.time ?: 0L

                    // Lakukan perbandingan dan kembalikan hasilnya (Boolean)
                    latestTipTimestamp > lastViewTimestamp
                } else {
                    // Jika tidak ada tips, tidak ada notifikasi baru
                    false
                }
            }.collect { hasNew ->
                // Hasil boolean dari combine akan dikirim ke sini.
                // Update state yang akan diobservasi oleh UI.
                _hasNewNotifications.value = hasNew
            }
        }
    }
}
