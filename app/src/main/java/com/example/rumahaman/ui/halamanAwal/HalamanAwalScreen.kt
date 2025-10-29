package com.example.rumahaman.ui.halamanAwal



import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController

@Composable
fun HalamanAwalScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.White)
                .border(
                    width = 2.dp,
                    color = Color.Red
                )
                .padding(16.dp)
            ,horizontalAlignment = Alignment.CenterHorizontally
            ,verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.White)
                    .border(2.dp, color = Color.Black)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
                ,verticalArrangement = Arrangement.Center
            ){
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Icon App",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(40.dp)
                    )
                }


                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "#RumahAman",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.White)
                    .border(2.dp, color = Color.Black)
                    .padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "Placeholder Konten", color = Color.Gray)
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(2.dp, Color.Red)
                .clip(RoundedCornerShape(topStart = 48.dp))
                .background(Color.Gray)
                .padding(36.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(text = "#RumahAman hadir untuk membantu para pengguna dan korban kekerasan  seksual yang tidak berani melapor  dan tidak tahu apa yang harus dilakukannya setelah mengalami kekerasan seksual."
                    , fontSize = 16.sp
                    , textAlign = TextAlign.Justify)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { /* TODO: Navigasi ke halaman berikutnya */ },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Bergabung",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "Lanjutkan",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHalamanAwalScreen() {
    HalamanAwalScreen(navController = rememberNavController())
}