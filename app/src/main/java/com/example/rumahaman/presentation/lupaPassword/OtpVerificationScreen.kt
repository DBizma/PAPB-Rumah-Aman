package com.example.rumahaman.presentation.lupaPassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rumahaman.R

@Composable
fun OtpVerificationScreen(navController: NavController) {

    val headerColor = Color(0xFFABD2CD)
    val buttonColor = Color(0xFF2E7D84)

    var otp by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.42f)
                .background(headerColor),
            contentAlignment = Alignment.Center
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // Icon
                Box(
                    modifier = Modifier
                        .size(95.dp)
                        .background(Color.White.copy(alpha = 0.25f), RoundedCornerShape(100.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_email_receiv),
                        contentDescription = "",
                        modifier = Modifier.size(45.dp)
                    )
                }

                Spacer(Modifier.height(18.dp))

                Text(
                    text = "OTP Verifikasi",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Masukkan OTP terkirim ke margarethap@gmail.com",
                    fontSize = 15.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }

        // BODY
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(35.dp))

            // OTP BOXES (4 digit)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(4) {
                    Box(
                        modifier = Modifier
                            .size(65.dp)
                            .border(2.dp, Color(0xFFD9E6E2), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = otp.getOrNull(it)?.toString() ?: "",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Tidak mendapatkan OTP?  Kirim ulang",
                fontSize = 15.sp,
                color = buttonColor
            )

            Spacer(Modifier.height(40.dp))

            // Verifikasi Button
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(buttonColor)
            ) {
                Text(
                    text = "Verifikasi",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(Modifier.height(25.dp))

            TextButton(onClick = { navController.popBackStack() }) {
                Text(
                    text = "Ganti alamat email",
                    color = buttonColor
                )
            }
        }
    }
}