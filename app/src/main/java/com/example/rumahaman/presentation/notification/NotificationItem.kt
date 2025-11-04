package com.example.rumahaman.presentation.notification

import androidx.annotation.DrawableRes
import com.example.rumahaman.R
import java.util.Date // <-- TAMBAHKAN IMPORT INI

sealed class NotificationItem(
    val id: String,
    @DrawableRes val imageRes: Int,
    val title: String,
    val description: String
) {
    // --- UBAH BAGIAN INI ---
    class Tip(
        id: String,
        title: String,
        description: String,
        val link: String,
        val createdAt: Date? // <-- TAMBAHKAN PROPERTI INI
    ) : NotificationItem(id, R.drawable.new_notif, title, description)

    // Kelas lain di bawah ini tidak perlu diubah
    class NewAccount(
        id: String
    ) : NotificationItem(id, R.drawable.new_acc, "Selamat, pendaftaran akun berhasil", "Kamu bisa menikmati fitur yang ada mulai sekarang")

    class Welcome(
        id: String,
        userName: String
    ) : NotificationItem(id, R.drawable.welcome, "Selamat datang $userName", "Lengkapi profilmu dulu yuk")
}
