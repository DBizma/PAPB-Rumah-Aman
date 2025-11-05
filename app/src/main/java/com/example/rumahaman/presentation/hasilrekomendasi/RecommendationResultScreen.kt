package com.example.rumahaman.presentation.hasilrekomendasi

import com.example.rumahaman.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TealPrimary = Color(0xFF2E7D84)
private val CardBackground = Color(0x66D9E6E2) // rgba(217,230,226,0.4)

@Composable
fun RecommendationResultScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                // sama seperti layout Figma: margin kiri/kanan 27dp
                .padding(horizontal = 27.dp, vertical = 16.dp)
        ) {
            // ep:back (166-823)
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clickable(onClick = onBackClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // "Layanan yang tepat untuk Anda:" (166-851)
            Text(
                text = buildAnnotatedString {
                    append("Layanan yang ")
                    withStyle(style = SpanStyle(color = TealPrimary)) {
                        append("tepat")
                    }
                    append(" untuk Anda:")
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.32).sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Rectangle 16 (166-850)
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(size = 10.dp),
                        ambientColor = Color(0x40000000),
                        spotColor = Color(0x40000000)
                    )
                    // dengan padding Column 27dp kiri/kanan,
                    // fillMaxWidth -> lebar kartu ≈ 376dp di iPhone,
                    // dan margin visual tetap 27dp di medium phone Android
                    .width(430.dp)
                    .height(932.dp)
                    .heightIn(min = 546.dp)
                    .background(
                        color = CardBackground,
                        shape = RoundedCornerShape(size = 10.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo layanan (171-785)
                    Image(
                        painter = painterResource(id = R.drawable.logo_hasilrekomendasi),
                        contentDescription = null,
                        modifier = Modifier.size(76.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Nama Layanan (162-1266, 162-1267)
                    Text(
                        text = "Nama Layanan:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.32).sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Biro Psikologi Lestari",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealPrimary,
                        letterSpacing = (-0.32).sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Deskripsi Layanan (162-1268, 162-1270)
                    Text(
                        text = "Deskripsi Layanan:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.32).sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Biro Psikologi Lestari merupakan sebuah biro layanan psikologi yang menyediakan layanan utama berupa konsultasi psikologis maupun psikiatri, tes psikologis untuk kepentingan pendidikan, dan tes psikologis untuk kepentingan rekrutmen.",
                        fontSize = 14.sp,
                        letterSpacing = (-0.28).sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Alamat Layanan (163-1272, 163-1273)
                    Text(
                        text = "Alamat Layanan:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.32).sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                    )
                    Text(
                        text = "The Serenity, Jl. Nginden Semolo No.21, RT.000/RW.00, Semolowaru, Kec. Sukolilo, Surabaya, Jawa Timur 60118",
                        fontSize = 14.sp,
                        letterSpacing = (-0.28).sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Kontak Layanan (163-1276, 163-1277)
                    Text(
                        text = "Kontak Layanan:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.32).sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                    )
                    Text(
                        text = "https://linktr.ee/BiroLestari",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light,
                        color = TealPrimary,
                        letterSpacing = (-0.32).sp,
                        textAlign = TextAlign.Start,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Medium Phone API 36"
)
@Composable
fun RecommendationResultScreenPreview() {
    MaterialTheme {
        RecommendationResultScreen()
    }
}
