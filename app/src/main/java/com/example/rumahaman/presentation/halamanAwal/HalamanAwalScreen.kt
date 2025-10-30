package com.example.rumahaman.presentation.halamanAwal


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.zIndex
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.example.rumahaman.R
import com.example.rumahaman.navigation.Routes
import com.google.common.io.Files.append


@Composable
fun HalamanAwalScreen(
    navController: NavHostController,
    viewModel: HalamanAwalViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2EBDD))
            .clipToBounds() // penting
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
        ) {
            // Bagian atas (logo + text)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Icon App",
                        modifier = Modifier.size(100.dp)
                    )
                    Text(
                        text = uiState.appName,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                }
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 48.dp))
                    .background(Color(0xFFD9E6E2))
                    .padding(36.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF9800) // oranye
                            )
                        ) {
                            append(uiState.appName)
                        }
                        append(" "+uiState.description)
                    },
                    fontSize = 18.sp,
                    textAlign = TextAlign.Justify
                )

                Button(
                    onClick = { navController.navigate(Routes.LOGIN_SCREEN) {
                                    launchSingleTop = true
                                 }
                              },
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(48.dp)
                        .align(Alignment.End),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D84))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            text = uiState.isJoining,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Lanjutkan",
                            tint = Color.White,
                        )
                    }
                }
            }
        }
        Image(
            painter = painterResource(id = R.drawable.crowdpeople),
            contentDescription = "Ilustrasi Komunitas",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .offset(y = -70.dp)
                .zIndex(-1f)

        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHalamanAwalScreen() {
    HalamanAwalScreen(navController = rememberNavController())
}