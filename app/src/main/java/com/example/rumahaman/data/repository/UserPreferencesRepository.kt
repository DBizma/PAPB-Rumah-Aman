package com.example.rumahaman.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Buat extension property untuk akses DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // Definisikan key untuk timestamp
    private object PreferencesKeys {
        val LAST_NOTIFICATION_VIEW = longPreferencesKey("last_notification_view_timestamp")
    }

    /**
     * Mengambil timestamp terakhir kali melihat notifikasi SEBAGAI SEBUAH FLOW.
     * Siapa pun yang 'collect' Flow ini akan mendapat update otomatis jika nilainya berubah.
     */
    val lastNotificationViewTimestampFlow: Flow<Long> = context.dataStore.data
        .map { preferences ->
            // Jika key tidak ada, kembalikan 0
            preferences[PreferencesKeys.LAST_NOTIFICATION_VIEW] ?: 0L
        }

    /**
     * Menyimpan timestamp saat ini sebagai waktu terakhir melihat notifikasi.
     * Fungsi ini adalah 'suspend' karena operasi DataStore bersifat asinkron.
     */
    suspend fun updateLastNotificationViewTimestamp() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_NOTIFICATION_VIEW] = System.currentTimeMillis()
        }
    }
}
    