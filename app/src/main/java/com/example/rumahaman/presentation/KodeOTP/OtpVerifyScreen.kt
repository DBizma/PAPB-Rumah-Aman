package com.example.rumahaman.presentation.KodeOTP

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OtpVerifyScreen(onBack: () -> Unit) {
    var otpCode by remember { mutableStateOf(List(4) { "" }.toMutableList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFABD2CD)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Icon(
            imageVector = Icons.Default.Smartphone,
            contentDescription = "Phone Icon",
            tint = Color.Black,
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "OTP Verifikasi",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Masukkan OTP terkirim ke +62 8993651888",
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = Color.DarkGray,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            otpCode.forEachIndexed { index, value ->
                OutlinedTextField(
                    value = value,
                    onValueChange = {
                        if (it.length <= 1) otpCode[index] = it
                    },
                    singleLine = true,
                    modifier = Modifier.width(60.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(onClick = { /* TODO: resend OTP */ }) {
            Text("Tidak mendapatkan OTP? Kirim ulang", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* TODO: verify action */ },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp)
        ) {
            Text("Verifikasi", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onBack) {
            Text("Ganti nomor telepon", color = Color.Black)
        }
    }
}