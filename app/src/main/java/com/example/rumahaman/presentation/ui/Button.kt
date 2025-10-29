package com.example.rumahaman.presentation.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rumahaman.presentation.ui.theme.RumahAmanTheme
import com.example.rumahaman.presentation.ui.theme.TealColor

/**
 * Komponen Button utama yang dapat digunakan kembali di seluruh aplikasi.
 *
 * @param text Teks yang akan ditampilkan di dalam tombol.
 * @param onClick Lambda yang akan dieksekusi saat tombol diklik.
 * @param modifier Modifier untuk kustomisasi Composable ini.
 */
@Composable
fun Button( // 1. Mengubah nama agar tidak bentrok dengan Button bawaan
    text: String, // 2. Menambahkan parameter untuk teks
    onClick: () -> Unit, // 3. Menambahkan parameter untuk aksi klik
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick, // Menggunakan parameter onClick
        modifier = modifier // Menggunakan modifier dari parameter
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = TealColor)
    ) {
        Text(
            text = text, // Menggunakan parameter text
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
    }
}

// Menambahkan Preview untuk melihat tampilan komponen secara terisolasi
@Preview(showBackground = true)
@Composable
private fun AuthButtonPreview() {
    RumahAmanTheme {
        Button(
            text = "Masuk",
            onClick = { }
        )
    }
}
