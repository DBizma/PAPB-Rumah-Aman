package com.example.rumahaman.presentation.hasilrekomendasi

import com.example.rumahaman.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rumahaman.presentation.recommendation.RecommendationViewModel

private val TealPrimary = Color(0xFF2E7D84)
private val CardBackground = Color(0x66D9E6E2) // rgba(217,230,226,0.4)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationResultScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: RecommendationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val recommendation = uiState.recommendation

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hasil Rekomendasi", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        if (recommendation == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Data rekomendasi tidak tersedia",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onBackClick) {
                        Text("Kembali ke Dashboard")
                    }
                }
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 27.dp)
                .verticalScroll(rememberScrollState())
        ) {
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

            // Card hasil rekomendasi
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(size = 10.dp),
                        ambientColor = Color(0x40000000),
                        spotColor = Color(0x40000000)
                    )
                    .background(
                        color = CardBackground,
                        shape = RoundedCornerShape(size = 10.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo layanan
                    Image(
                        painter = painterResource(id = R.drawable.logo_hasilrekomendasi),
                        contentDescription = null,
                        modifier = Modifier.size(76.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Nama Layanan
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
                        text = recommendation.service.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealPrimary,
                        letterSpacing = (-0.32).sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Deskripsi Layanan
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
                        text = recommendation.service.description,
                        fontSize = 14.sp,
                        letterSpacing = (-0.28).sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Alamat Layanan
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
                        text = recommendation.service.address,
                        fontSize = 14.sp,
                        letterSpacing = (-0.28).sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Kontak Layanan
                    Text(
                        text = "Kontak Layanan:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.32).sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                    )
                    
                    // URL
                    if (recommendation.service.contact.url.isNotBlank()) {
                        Text(
                            text = recommendation.service.contact.url,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light,
                            color = TealPrimary,
                            letterSpacing = (-0.28).sp,
                            textAlign = TextAlign.Start,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    
                    // Phone
                    if (recommendation.service.contact.phone.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Telepon: ${recommendation.service.contact.phone}",
                            fontSize = 14.sp,
                            letterSpacing = (-0.28).sp,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    
                    // Email
                    if (recommendation.service.contact.email.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Email: ${recommendation.service.contact.email}",
                            fontSize = 14.sp,
                            color = TealPrimary,
                            letterSpacing = (-0.28).sp,
                            textAlign = TextAlign.Start,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
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
