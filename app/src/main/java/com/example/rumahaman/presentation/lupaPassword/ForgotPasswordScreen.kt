package com.example.rumahaman.presentation.lupaPassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.rumahaman.navigation.Routes

@Composable
fun ForgotPasswordScreen(
    navController: NavController
) {
    var email by remember { mutableStateOf("") }

    val headerColor = Color(0xFFABD2CD)
    val buttonColor = Color(0xFF2E7D84)
    val borderColor = Color(0xFFD9E6E2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // ---------------- HEADER HIJAU (AUTO HEIGHT 42%) ----------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.42f)
                .background(headerColor),
            contentAlignment = Alignment.Center
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // Ikon Download/Email
                Box(
                    modifier = Modifier
                        .size(95.dp)
                        .background(Color.White.copy(alpha = 0.25f), RoundedCornerShape(100.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_email_send),
                        contentDescription = "Email Icon",
                        modifier = Modifier.size(45.dp)
                    )
                }

                Spacer(Modifier.height(18.dp))

                Text(
                    text = "Lupa Password",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Kami akan mengirimkan One Time Password\npada alamat email ini.",
                    fontSize = 15.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }

        // ---------------- CARD PUTIH (DENGAN LENGKUNGAN OTOMATIS) ----------------
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 30.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(35.dp))

                Text(
                    text = "Masukkan Alamat Email",
                    fontSize = 15.sp,
                    color = Color.Gray
                )

                Spacer(Modifier.height(6.dp))

                // Email Input
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = {
                        Text(
                            "margarethap@gmail.com",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = borderColor,
                        focusedBorderColor = buttonColor,
                        unfocusedTextColor = Color.Black,
                        focusedTextColor = Color.Black
                    )
                )

                Spacer(Modifier.height(40.dp))

                // Tombol Dapatkan OTP
                Button(
                    onClick = {
                        navController.navigate("otp_verification")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(buttonColor)
                ) {
                    Text(
                        text = "Dapatkan OTP",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(Modifier.height(20.dp))

                // Back to login
                TextButton(onClick = { navController.navigate(Routes.LOGIN_SCREEN) }) {
                    Text(
                        text = "Kembali ke Login",
                        color = buttonColor,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}