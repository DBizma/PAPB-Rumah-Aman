package com.example.rumahaman.presentation.dashboard


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.rumahaman.R
import androidx.compose.ui.unit.sp

@Composable
fun IconUser() {
    Image(
        painter = painterResource(id = R.drawable.dbicon), // nama file XML hasil impor SVG
        contentDescription = "Logo SVG",
        modifier = Modifier.size(20.dp)
    )
}

@Composable
fun DashboardScreen() {
    Column (
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column (
            modifier = Modifier
                .weight(1.2f)
                .fillMaxWidth()
                .background(Color.White)
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            // 1. BAGIAN ATAS
            Column (
                modifier = Modifier
                    .weight(0.8f)
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = TriangleBottomShape(triangleDepth = 36.dp, cornerRadius = 12.dp)
                    )
                    .clip(shape = TriangleBottomShape(cornerRadius = 12.dp, triangleDepth = 36.dp))
                    .background(Color(0xFFABD2CD))
                    .padding(24.dp)
                    ,
            ){
                Row (
                    Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Column (Modifier.weight(1.2f)
                        .fillMaxHeight(),
                        ){
                        Row(
                            verticalAlignment = Alignment.CenterVertically // Agar teks dan ikon sejajar di tengah
                        ) {
                            Text("Hi "+ "User, ", // Hapus bagian "(Icon)"
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            IconUser()
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text("Sampaikan apa yang \n" +
                                "kamu rasakan \n" +
                                "kepada kami",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                        )
                }
                    Box(Modifier.weight(1f)
                        .fillMaxHeight()){
                        Image(painter = painterResource(id = R.drawable.dbatas),
                            contentDescription = "GambarDashboard1",
                            contentScale = ContentScale.FillWidth,
                            alignment = Alignment.BottomCenter,
                            modifier = Modifier
                                .fillMaxSize()
                                .scale(1.5f)
                        )
                    }
                }

            }

            // 2. BAGIAN TENGAH
            Row (
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxWidth()
                    .offset(y = (-12).dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp) // Jarak antar item horizontal
            ){
                //Kiri
                Column( // <-- Diubah dari Box menjadi Column
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .shadow(
                            elevation = 8.dp,
                            shape = DiagonalCutTopRightShape(cutSize = 36.dp, cornerRadius = 12.dp)
                        )
                        // 1. Perbaiki sintaks clip dengan nama parameter
                        .clip(DiagonalCutTopRightShape(cutSize = 36.dp, cornerRadius = 12.dp))
                        .background(Color(0xFFEFD2C3))
                        .clickable {
                            println("Tombol Bagian 2B ditekan!")
                        },
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Bottom // Mulai konten dari atas
                ) {
                    Box(modifier = Modifier.weight(1f)
                        .padding(top = 24.dp),
                        contentAlignment = Alignment.CenterStart){
                        Text(
                            text = "Sistem\nRekomendasi",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                        )
                    }
                    Box(modifier = Modifier.weight(1f)){
                        Image(
                            painter = painterResource(id = R.drawable.dbkiri), // Asumsi nama file gambar
                            contentDescription = "Gambar Sistem Rekomendasi",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }



                //Kanan
                Column( // <-- Diubah dari Box menjadi Column
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .shadow(
                            elevation = 8.dp,
                            shape = DiagonalCutTopLeftShape(cutSize = 36.dp, cornerRadius = 12.dp)
                        )
                        .clip(DiagonalCutTopLeftShape(cutSize = 36.dp, cornerRadius = 12.dp))
                        .background(Color(0xFFD9E6E2))
                        .clickable {
                            println("Tombol Bagian 2B ditekan!")
                        },
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Box(modifier = Modifier.weight(1f)
                        .padding(top = 24.dp),
                        contentAlignment = Alignment.CenterStart){
                        Text(
                            text = "Chatting Bot",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                        )
                    }
                    Box(modifier = Modifier.weight(1f)){
                        Image(
                            painter = painterResource(id = R.drawable.dbkanan),
                            contentDescription = "Gambar Chatting Bot",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()

                        )
                    }
                }


            }
        }

        // 3. KOMPONEN BAWAH (Memakan 1/2 Ruang Vertikal)
        Column (
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()

        ){
            Box( modifier = Modifier.padding(vertical = 8.dp)){
                Text(
                    text = "Tips Populer:",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column (
                modifier = Modifier
                    .weight(1f)
                ,verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                // --- KOTAK PERTAMA ---
                Box(
                    modifier = Modifier
                        // .padding( vertical = 8.dp) // Padding sebaiknya di luar Box
                        .weight(1f)
                        .fillMaxWidth()
                        // 1. Potong bentuknya DULU
                        .clip(RoundedCornerShape(12 .dp))
                        // 2. BARU warnai bentuk yang sudah terpotong
                        .background(Color(0xFFD9E6E2))
                ){
                    Text(
                        text = "Tips 1",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }

                // --- KOTAK KEDUA ---
                Box(
                    modifier = Modifier
                        // .padding( vertical = 8.dp) // spacedBy sudah menangani jarak
                        .weight(1f)
                        .fillMaxWidth()
                        // Urutan yang benar
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFD9E6E2))
                ){
                    Text(
                        text = "Tips 2",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDashboardScreen() {
    DashboardScreen()
}


// @Composable
// fun DashboardScreen(
//     navController: NavController
// ) {
//     Column(
//         modifier = Modifier
//             .fillMaxSize()
//             .padding(24.dp),
//         horizontalAlignment = Alignment.CenterHorizontally,
//         verticalArrangement = Arrangement.Center
//     ) {
//         Text(
//             text = "Dashboard",
//             fontSize = 32.sp,
//             fontWeight = FontWeight.Bold,
//             color = Color.Black
//         )

//         Spacer(modifier = Modifier.height(16.dp))

//         Text(
//             text = "Selamat datang di #RumahAman",
//             fontSize = 16.sp,
//             color = Color.Gray
//         )

//         Spacer(modifier = Modifier.height(48.dp))

//         // Tombol ke ChatBot
//         Button(
//             onClick = {
//                 navController.navigate(Routes.CHATBOT_SCREEN)
//             },
//             modifier = Modifier
//                 .fillMaxWidth()
//                 .height(56.dp),
//             colors = ButtonDefaults.buttonColors(
//                 containerColor = Color(0xFF008B8B) // Teal color
//             ),
//             shape = MaterialTheme.shapes.medium
//         ) {
//             Text(
//                 text = "Chatting Bot",
//                 fontSize = 18.sp,
//                 fontWeight = FontWeight.Bold
//             )
//         }

//         Spacer(modifier = Modifier.height(16.dp))

//         Text(
//             text = "Klik tombol di atas untuk memulai percakapan dengan chatbot konselor kami",
//             fontSize = 14.sp,
//             color = Color.Gray,
//             modifier = Modifier.padding(horizontal = 32.dp)
//         )
//     }
// }
