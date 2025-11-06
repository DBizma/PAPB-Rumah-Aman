package com.example.rumahaman.presentation.notification

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rumahaman.presentation.ui.NotificationCard
import com.example.rumahaman.presentation.ui.theme.LinkColor
import com.example.rumahaman.presentation.ui.theme.RumahAmanTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

// Helper function untuk format tanggal
@RequiresApi(Build.VERSION_CODES.O)
fun formatDateHeader(date: LocalDate): String {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    return when (date) {
        today -> "Hari ini"
        yesterday -> "Kemarin"
        else -> {
            // Format untuk tanggal yang lebih lama, e.g., "Selasa, 4 November 2025"
            date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale("id", "ID")))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NotificationScreen(
    navController: NavController,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifikasi", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.groupedNotifications.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Belum ada notifikasi")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Iterasi melalui map yang sudah dikelompokkan
                state.groupedNotifications.forEach { (date, notifications) ->

                    // Buat header untuk setiap grup tanggal
                    stickyHeader {
                        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).background(MaterialTheme.colorScheme.surface)) {
                            // Teks "Kamu punya 1 notifikasi hari ini" hanya untuk grup "Hari ini"
                            if (formatDateHeader(date) == "Hari ini") {
                                val count = notifications.filterIsInstance<NotificationItem.Tip>().size
                                if (count > 0) {
                                    val annotatedString = buildAnnotatedString {
                                        append("Kamu punya ")
                                        withStyle(style = SpanStyle(color = LinkColor, fontWeight = FontWeight.Bold)) {
                                            append("$count notifikasi")
                                        }
                                        append(" hari ini")
                                    }
                                    Text(
                                        text = annotatedString,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )
                                }
                            } else {
                                // Untuk hari lain, tampilkan tanggalnya saja
                                Text(
                                    text = formatDateHeader(date),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                        }
                    }

                    // Tampilkan semua item notifikasi dalam grup tanggal ini
                    items(notifications, key = { it.id }) { notification ->
                        NotificationCard(item = notification)
                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    RumahAmanTheme {
        NotificationScreen(navController = rememberNavController())
    }
}

