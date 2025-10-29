package com.example.rumahaman.presentation.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
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
// Impor nama 'Button' dari material3 dengan alias agar tidak konflik
import androidx.compose.material3.Button as MaterialButton

/**
 * Komponen Button utama yang dapat digunakan kembali di seluruh aplikasi.
 *
 * @param text Teks yang akan ditampilkan di dalam tombol.
 * @param onClick Lambda yang akan dieksekusi saat tombol diklik.
 * @param modifier Modifier untuk kustomisasi Composable ini.
 * @param enabled Mengontrol status aktif/nonaktif tombol.
 */
@Composable
fun Button( // 1. NAMA FUNGSI DIKEMBALIKAN menjadi Button
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true // Parameter 'enabled' tetap ada
) {
    // Menggunakan MaterialButton (Button dari Material3 yang sudah diberi alias)
    MaterialButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = TealColor),
        enabled = enabled // Menggunakan parameter 'enabled'
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
    }
}

// Menambahkan Preview untuk melihat tampilan komponen secara terisolasi
@Preview(showBackground = true, name = "Enabled Button")
@Composable
private fun ButtonEnabledPreview() {
    RumahAmanTheme {
        Button( // Menggunakan nama Button
            text = "Masuk",
            onClick = { }
        )
    }
}

@Preview(showBackground = true, name = "Disabled Button")
@Composable
private fun ButtonDisabledPreview() {
    RumahAmanTheme {
        Button( // Menggunakan nama Button
            text = "Masuk",
            onClick = { },
            enabled = false // Preview untuk status nonaktif
        )
    }
}
