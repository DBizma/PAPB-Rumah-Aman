package com.example.rumahaman.presentation.dashboard

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

class DiagonalCutTopRightShape(
    private val cutSize: Dp,
    private val cornerRadius: Dp = Dp(0f)
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val cutPx = with(density) { cutSize.toPx() }
        val radiusPx = with(density) { cornerRadius.toPx() }

        // 🔹 Path dasarnya tetap seperti punyamu
        val basePath = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, cutPx)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }

        // 🔹 Tambahkan sudut melengkung tanpa mengubah bentuk dasar
        val rounded = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(0f, 0f, size.width, size.height),
                    cornerRadius = CornerRadius(radiusPx, radiusPx)
                )
            )
        }

        // 🔹 Gabungkan kedua path — hasilnya tetap bentuk diagonal tapi dengan sudut melengkung
        // This is the corrected line
        basePath.op(path1 = basePath, path2 = rounded, operation = PathOperation.Intersect)

        return Outline.Generic(basePath)
    }
}
