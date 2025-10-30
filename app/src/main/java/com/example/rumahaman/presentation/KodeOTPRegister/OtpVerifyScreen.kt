package com.example.rumahaman.presentation.KodeOTPRegister

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rumahaman.R
import com.example.rumahaman.presentation.ui.theme.RumahAmanTheme

@Composable
fun OtpVerifyScreen(
    phoneNumber: String,
    onVerifyClick: (String) -> Unit,
    // Tambahkan dua aksi ini untuk fungsionalitas penuh
    onResendOtpClick: () -> Unit,
    onChangePhoneClick: () -> Unit
) {
    var otpValue by remember { mutableStateOf(TextFieldValue("", TextRange(0))) }

    // Gunakan Box untuk menumpuk latar belakang dan konten
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFABD2CD)) // Warna latar belakang atas
    ) {
        // --- Bagian Atas (Header) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(50.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.otp_ver),
                    contentDescription = "OTP Icon",
                    modifier = Modifier
                        .scale(0.8f), // ubah ukuran di sini
                    contentScale = ContentScale.Fit // menyesuaikan proporsi gambar
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "OTP Verifikasi",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Masukkan OTP terkirim ke $phoneNumber",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.DarkGray,
                modifier = Modifier.padding(horizontal = 40.dp)
            )
        }

        // --- Bagian Bawah (Kartu Putih) ---
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 300.dp),
            shape = RoundedCornerShape(topStart = 40.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 32.dp, end = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(60.dp))
                // Input OTP Kustom
                BasicTextField(
                    value = otpValue,
                    onValueChange = {
                        if (it.text.length <= 4) {
                            otpValue = it
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    decorationBox = {
                        Row(horizontalArrangement = Arrangement.Center) {
                            repeat(4) { index ->
                                val char = when {
                                    index < otpValue.text.length -> otpValue.text[index]
                                    else -> ""
                                }
                                val isFocused = index == otpValue.text.length
                                Box(
                                    modifier = Modifier
                                        .size(width = 50.dp, height = 60.dp)
                                        .border(
                                            width = 1.dp,
                                            color = if (isFocused) Color(0xFF4B8079) else Color.LightGray,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .background(
                                            color = Color.LightGray.copy(alpha = 0.1f),
                                            shape = RoundedCornerShape(8.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = char.toString(),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Teks Kirim Ulang
                Row {
                    Text("Tidak mendapatkan OTP? ", color = Color.Gray)
                    Text(
                        text = "Kirim ulang",
                        color = Color(0xFF4B8079),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onResendOtpClick() }
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Tombol Verifikasi
                Button(
                    onClick = { onVerifyClick(otpValue.text) },
                    enabled = otpValue.text.length == 4,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4B8079)
                    )
                ) {
                    Text("Verifikasi", fontSize = 16.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Teks Ganti Nomor Telepon
                Text(
                    text = "Ganti nomor telepon",
                    color = Color(0xFF4B8079),
                    modifier = Modifier.clickable { onChangePhoneClick() }
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Composable
fun OtpVerifyScreenPreview() {
    RumahAmanTheme {
        // Berikan nilai dummy agar preview bisa berjalan
        OtpVerifyScreen(
            phoneNumber = "+628993651888",
            onVerifyClick = {},
            onResendOtpClick = {},
            onChangePhoneClick = {}
        )
    }
}
