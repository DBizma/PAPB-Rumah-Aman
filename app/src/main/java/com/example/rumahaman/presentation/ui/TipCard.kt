package com.example.rumahaman.presentation.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rumahaman.R
import com.example.rumahaman.presentation.notification.NotificationItem
import com.example.rumahaman.presentation.ui.theme.RumahAmanTheme
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipCard(
    tip: NotificationItem.Tip,
    onTipClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showInvalidLinkDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp), // Tinggi card diperbesar
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F5F4)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        onClick = {
            try {
                onTipClicked(tip.id)
                val openUrlIntent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(tip.link)
                }
                context.startActivity(openUrlIntent)
            } catch (e: Exception) {
                println("Error opening link: ${e.message}")
                showInvalidLinkDialog = true
            }
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Gambar di sisi kiri (40% lebar)
            Image(
                painter = painterResource(id = tip.imageRes),
                contentDescription = tip.title,
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            // Konten di kanan (60% lebar)
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Title
                Text(
                    text = tip.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black,
                    maxLines = 2
                )
                
                // Counter dengan icon mata (warna orange)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFFF9966)
                    )
                    Text(
                        text = "${tip.viewCount} orang melihat ini",
                        fontSize = 12.sp,
                        color = Color(0xFFFF9966),
                        fontWeight = FontWeight.Medium
                    )
                }
                
                // Button "klik disini" dengan stroke orange
                OutlinedButton(
                    onClick = {
                        try {
                            onTipClicked(tip.id)
                            val openUrlIntent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse(tip.link)
                            }
                            context.startActivity(openUrlIntent)
                        } catch (e: Exception) {
                            println("Error opening link: ${e.message}")
                            showInvalidLinkDialog = true
                        }
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color(0xFFFF9966)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.height(36.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "klik disini",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    // Dialog error jika link tidak valid
    if (showInvalidLinkDialog) {
        AlertDialog(
            onDismissRequest = { showInvalidLinkDialog = false },
            icon = { Icon(Icons.Default.Info, contentDescription = "Error Icon") },
            title = { Text(text = "Link Tidak Valid") },
            text = { Text("Maaf, tautan yang coba Anda buka tidak dapat diakses saat ini.") },
            confirmButton = {
                TextButton(onClick = { showInvalidLinkDialog = false }) {
                    Text("Mengerti")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TipCardPreview() {
    RumahAmanTheme {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TipCard(
                tip = NotificationItem.Tip(
                    id = "1",
                    title = "Latihan Pernapasan Yang Bisa Meredakan Kecemasan",
                    description = "Tips untuk mengatasi kecemasan",
                    link = "https://example.com/tips1",
                    createdAt = Date(),
                    viewCount = 24
                ),
                onTipClicked = {}
            )
            
            TipCard(
                tip = NotificationItem.Tip(
                    id = "2",
                    title = "Cara Mengenali Tanda-Tanda Kekerasan",
                    description = "Panduan penting untuk keamanan",
                    link = "https://example.com/tips2",
                    createdAt = Date(),
                    viewCount = 15
                ),
                onTipClicked = {}
            )
        }
    }
}
