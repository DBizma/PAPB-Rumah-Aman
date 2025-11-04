package com.example.rumahaman.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.rumahaman.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // Fungsi ini akan terpanggil ketika ada pesan baru masuk SAAT APLIKASI DI LATAR BELAKANG/TERTUTUP
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Dapatkan judul dan isi pesan dari notifikasi FCM
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body

        if (title != null && body != null) {
            showNotification(title, body)
        }
    }

    private fun showNotification(title: String, text: String) {
        val channelId = "RUMAH_AMAN_CHANNEL"
        val channelName = "Notifikasi Rumah Aman"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Buat channel notifikasi (hanya diperlukan untuk Android 8.0 Oreo ke atas)
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)

        // Buat notifikasi
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo) // Ganti dengan ikon notifikasi Anda
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // Notifikasi hilang saat diklik

        // Tampilkan notifikasi
        with(NotificationManagerCompat.from(this)) {
            // Periksa izin notifikasi sebelum menampilkan (untuk Android 13+)
            if (ActivityCompat.checkSelfPermission(this@MyFirebaseMessagingService, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notify(1, builder.build()) // ID notifikasi
            }
        }
    }

    // Fungsi ini terpanggil ketika FCM memberikan token baru untuk perangkat ini
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Di aplikasi nyata, Anda akan mengirim token ini ke server Anda
        // agar bisa mengirim notifikasi ke perangkat spesifik.
        // Contoh: sendTokenToServer(token)
    }
}
    