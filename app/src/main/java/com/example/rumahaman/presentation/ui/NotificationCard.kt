package com.example.rumahaman.presentation.ui

import android.content.ActivityNotFoundException // <-- IMPORT BARU
import android.content.Intent
import android.net.Uri
import android.widget.Toast // <-- IMPORT BARU (Alternatif jika pop-up terlalu kompleks)
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info // <-- IMPORT BARU (untuk ikon dialog)
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.AlertDialog // <-- IMPORT BARU
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton // <-- IMPORT BARU
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue // <-- IMPORT BARU
import androidx.compose.runtime.mutableStateOf // <-- IMPORT BARU
import androidx.compose.runtime.remember // <-- IMPORT BARU
import androidx.compose.runtime.setValue // <-- IMPORT BARU
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rumahaman.R
import com.example.rumahaman.presentation.notification.NotificationItem
import com.example.rumahaman.presentation.ui.theme.LinkColor
import com.example.rumahaman.presentation.ui.theme.RumahAmanTheme
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationCard(
    item: NotificationItem,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // --- TAMBAHKAN STATE UNTUK DIALOG ---
    // State untuk mengontrol visibilitas dialog error
    var showInvalidLinkDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F5F4)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        onClick = {
            if (item is NotificationItem.Tip) {
                // --- TAMBAHKAN TRY-CATCH DI SINI ---
                try {
                    // Buat intent untuk membuka URL di browser
                    val openUrlIntent = Intent(Intent.ACTION_VIEW).apply {
                        // Uri.parse akan melempar exception jika format URL salah
                        data = Uri.parse(item.link)
                    }
                    // Jalankan intent
                    context.startActivity(openUrlIntent)
                } catch (e: Exception) {
                    // Tangkap semua jenis error (misal: format URL salah, tidak ada browser)
                    println("Error opening link: ${e.message}") // Untuk logging di Logcat
                    showInvalidLinkDialog = true // Tampilkan dialog error
                }
            }
        }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gambar di sisi kiri
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.title,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Teks di tengah
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
            }

            // Ikon link jika notifikasi adalah Tipe 'Tip'
            if (item is NotificationItem.Tip) {
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.Link,
                    contentDescription = "Link",
                    tint = LinkColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

    // --- TAMBAHKAN DIALOG DI SINI ---
    // Dialog ini akan muncul jika showInvalidLinkDialog bernilai true
    if (showInvalidLinkDialog) {
        AlertDialog(
            onDismissRequest = {
                // Aksi saat pengguna mengklik di luar dialog
                showInvalidLinkDialog = false
            },
            // Ikon di bagian atas dialog
            icon = { Icon(Icons.Default.Info, contentDescription = "Error Icon") },
            // Judul dialog
            title = { Text(text = "Link Tidak Valid") },
            // Isi pesan dialog
            text = { Text("Maaf, tautan yang coba Anda buka tidak dapat diakses saat ini.") },
            // Tombol konfirmasi
            confirmButton = {
                TextButton(
                    onClick = {
                        showInvalidLinkDialog = false
                    }
                ) {
                    Text("Mengerti")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationCardPreview() {
    RumahAmanTheme {
        Column(            modifier = Modifier
            .background(Color.White)
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Preview untuk setiap jenis notifikasi
            NotificationCard(
                item = NotificationItem.Tip(
                    id = "1",
                    title = "Tips baru, baca sekarang!",
                    description = "Lakukan 5 Tips Ini untuk Atasi Trauma Masa Lalu",
                    link = "https://example.com",
                    createdAt = Date()
                )
            )
            NotificationCard(
                item = NotificationItem.Welcome(
                    id = "2",
                    userName = "Margaretha"
                )
            )
            NotificationCard(
                item = NotificationItem.NewAccount(id = "3")
            )
        }
    }
}