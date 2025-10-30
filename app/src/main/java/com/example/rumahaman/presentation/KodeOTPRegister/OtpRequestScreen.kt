package com.example.rumahaman.presentation.KodeOTPRegister

import android.provider.CalendarContract
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rumahaman.R
import com.example.rumahaman.presentation.ui.theme.RumahAmanTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpRequestScreen(onGetOtpClick: (String) -> Unit) {
    var phoneNumber by remember { mutableStateOf("") }

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
            // Lingkaran di belakang ikon
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(50.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.otp_req),
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
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = buildAnnotatedString {
                    append("Kami akan mengirimkan ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                        append("One Time Password")
                    }
                    append(" pada nomor ponsel ini.")
                },
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 40.dp)
            )
        }

        // --- Bagian Bawah (Kartu Putih) ---
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 300.dp), // Sesuaikan posisi kartu dari atas
            shape = RoundedCornerShape(
                topEnd = 40.dp
            ), // Sudut melengkung hanya di atas
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 32.dp, end = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(80.dp))
                // TextField tanpa border, hanya label dan garis bawah

                Text(
                    text = "Masukkan Nomor Telepon",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = {Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+62-...-...-....",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    prefix = { Text("+62 ", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.LightGray,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = Color.Gray
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(modifier = Modifier.height(120.dp))

                Button(
                    onClick = { onGetOtpClick("+62${phoneNumber.trim()}") },
                    enabled = phoneNumber.isNotBlank(),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4B8079) // Warna tombol sesuai desain
                    )
                ) {
                    Text("Dapatkan OTP", fontSize = 22.sp, color = Color.White)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun OtpRequestScreenPreview() {
    RumahAmanTheme {
        OtpRequestScreen(onGetOtpClick = {})
    }
}
