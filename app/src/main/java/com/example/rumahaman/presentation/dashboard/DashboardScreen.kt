package com.example.rumahaman.presentation.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.rumahaman.R
import com.example.rumahaman.navigation.Routes
import com.example.rumahaman.presentation.ui.TipCard
import com.google.firebase.auth.FirebaseAuth


@Composable
fun IconUser() {
    Image(
        painter = painterResource(id = R.drawable.dbicon), // nama file XML hasil impor SVG
        contentDescription = "Logo SVG",
        modifier = Modifier.size(20.dp)
    )
}

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    
    // Refresh user name when screen appears
    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.refreshUserName()
    }
    
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
                            Text("Hi ${uiState.userName}, ", // Gunakan userName dari ViewModel
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
                            navController.navigate(Routes.RECOMMENDATION_SCREEN)
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
                            navController.navigate(Routes.CHATBOT_SCREEN)
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
            
            // Show skeleton or content
            if (uiState.popularTips.isEmpty()) {
                // Skeleton loader
                Column (
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    repeat(2) {
                        SkeletonTipCard()
                    }
                }
            } else {
                Column (
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ){
                    uiState.popularTips.forEach { tip ->
                        TipCard(
                            tip = tip,
                            onTipClicked = { tipId ->
                                viewModel.onTipClicked(tipId)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun SkeletonTipCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF0F5F4))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Skeleton image
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE0E0E0))
            )
            
            // Skeleton content
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Skeleton title
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFE0E0E0))
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFE0E0E0))
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Skeleton counter
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFE0E0E0))
                    )
                }
                
                // Skeleton button
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFE0E0E0))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDashboardScreen() {
    DashboardScreen(navController = androidx.navigation.compose.rememberNavController())
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
