// File: presentation/dashboard/TriangleBottomShape.kt
package com.example.rumahaman.presentation.dashboard

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class TriangleBottomShape(
    private val cornerRadius: Dp,
    private val triangleDepth: Dp // Seberapa dalam potongan segitiganya
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        // Konversi nilai Dp ke pixel
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }
        val triangleDepthPx = with(density) { triangleDepth.toPx() }

        val path = Path().apply {
            // Mulai dari sudut kiri atas
            moveTo(0f, cornerRadiusPx)

            // Gambar busur untuk sudut kiri atas yang tumpul
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(0f, 0f, 2 * cornerRadiusPx, 2 * cornerRadiusPx),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // Garis lurus ke kanan atas
            lineTo(size.width - cornerRadiusPx, 0f)

            // Gambar busur untuk sudut kanan atas yang tumpul
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(size.width - 2 * cornerRadiusPx, 0f, size.width, 2 * cornerRadiusPx),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // Garis lurus ke kanan bawah
            lineTo(size.width, size.height - triangleDepthPx)

            // --- Bagian Segitiga di Bawah ---
            // Gambar garis ke puncak segitiga (tengah bawah)
            lineTo(size.width / 2, size.height)

            // Gambar garis dari puncak segitiga ke kiri bawah
            lineTo(0f, size.height - triangleDepthPx)

            // Tutup path kembali ke titik awal (sudut kiri atas)
            close()
        }
        return Outline.Generic(path)
    }
}
