package com.example.rumahaman.presentation.recommendation



import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

// -----------------------------
// UI Model (UI-only; tanpa domain)
// -----------------------------
enum class Gender { LakiLaki, Perempuan }
enum class ViolenceType { Fisik, Mental, Keduanya }
enum class ServiceType { Psikologis, Hukum, Sosial, Medis, Shelter }

data class RecommendationFilters(
    val gender: Gender? = null,
    val age: String = "",
    val province: String = "",
    val violenceType: ViolenceType? = null,
    val serviceType: ServiceType? = null
)

data class RecommendationCardUI(
    val id: String,
    val serviceName: String,
    val serviceType: ServiceType,
    val province: String,
    val description: String,
    val contactLink: String
)

// -----------------------------
// Screen Root
// -----------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationScreen(
    onBack: (() -> Unit)? = null
) {
    var filters by remember { mutableStateOf(RecommendationFilters()) }
    var showResults by remember { mutableStateOf(false) }

    // Dummy data untuk tampilan hasil (UI saja)
    val dummyResults = remember {
        listOf(
            RecommendationCardUI(
                id = "1",
                serviceName = "Layanan Konseling Sehat Jiwa",
                serviceType = ServiceType.Psikologis,
                province = "Jawa Timur",
                description = "Konseling gratis dan rujukan psikolog untuk kasus kekerasan.",
                contactLink = "https://bit.ly/konseling-sehat"
            ),
            RecommendationCardUI(
                id = "2",
                serviceName = "Bantuan Hukum Perempuan & Anak",
                serviceType = ServiceType.Hukum,
                province = "Jawa Timur",
                description = "Pendampingan hukum, pelaporan, dan advokasi darurat.",
                contactLink = "https://bit.ly/bantuan-hukum"
            ),
            RecommendationCardUI(
                id = "3",
                serviceName = "Pusat Layanan Terpadu (Shelter)",
                serviceType = ServiceType.Shelter,
                province = "Jawa Barat",
                description = "Tempat aman sementara, rujukan medis, dan konseling.",
                contactLink = "https://bit.ly/shelter-aman"
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sistem Rekomendasi") },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Crossfade(
            targetState = showResults,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) { isResult ->
            if (isResult) {
                RecommendationResultList(
                    filters = filters,
                    results = dummyResults.filter { item ->
                        (filters.province.isBlank() || item.province == filters.province) &&
                                (filters.serviceType == null || item.serviceType == filters.serviceType)
                    },
                    onChangeFilter = { showResults = false }
                )
            } else {
                RecommendationForm(
                    filters = filters,
                    onFiltersChange = { filters = it },
                    onSubmit = {
                        // Validasi minimal (UI)
                        val ok = filters.gender != null &&
                                filters.age.toIntOrNull() != null &&
                                filters.province.isNotBlank() &&
                                filters.violenceType != null &&
                                filters.serviceType != null
                        if (ok) showResults = true
                    }
                )
            }
        }
    }
}

// -----------------------------
// Form Filter
// -----------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecommendationForm(
    filters: RecommendationFilters,
    onFiltersChange: (RecommendationFilters) -> Unit,
    onSubmit: () -> Unit
) {
    val focus = LocalFocusManager.current
    val provinces = listOf(
        "Jawa Timur", "Jawa Tengah", "Jawa Barat", "DKI Jakarta",
        "DI Yogyakarta", "Bali", "Sumatera Utara"
    )

    var provinceExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header teks
        Text(
            text = "Isi data berikut untuk menampilkan layanan yang sesuai.",
            style = MaterialTheme.typography.bodyMedium
        )

        // Gender
        Text("Jenis Kelamin", style = MaterialTheme.typography.titleSmall)
        FlowChipsRow(
            options = listOf("Laki-laki", "Perempuan"),
            selectedIndex = when (filters.gender) {
                Gender.LakiLaki -> 0
                Gender.Perempuan -> 1
                else -> -1
            },
            onSelected = {
                onFiltersChange(filters.copy(gender = if (it == 0) Gender.LakiLaki else Gender.Perempuan))
            }
        )

        // Umur
        OutlinedTextField(
            value = filters.age,
            onValueChange = { onFiltersChange(filters.copy(age = it.take(3).filter { c -> c.isDigit() })) },
            label = { Text("Umur") },
            placeholder = { Text("Contoh: 21") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Provinsi
        ExposedDropdownMenuBox(
            expanded = provinceExpanded,
            onExpandedChange = { provinceExpanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = filters.province,
                onValueChange = { /* read-only via menu */ },
                readOnly = true,
                label = { Text("Provinsi") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = provinceExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = provinceExpanded,
                onDismissRequest = { provinceExpanded = false }
            ) {
                provinces.forEach { prov ->
                    DropdownMenuItem(
                        text = { Text(prov) },
                        onClick = {
                            onFiltersChange(filters.copy(province = prov))
                            provinceExpanded = false
                            focus.clearFocus()
                        }
                    )
                }
            }
        }

        // Jenis Kekerasan
        Text("Jenis Kekerasan", style = MaterialTheme.typography.titleSmall)
        FlowChipsRow(
            options = listOf("Fisik", "Mental", "Keduanya"),
            selectedIndex = when (filters.violenceType) {
                ViolenceType.Fisik -> 0
                ViolenceType.Mental -> 1
                ViolenceType.Keduanya -> 2
                else -> -1
            },
            onSelected = {
                val v = when (it) {
                    0 -> ViolenceType.Fisik
                    1 -> ViolenceType.Mental
                    else -> ViolenceType.Keduanya
                }
                onFiltersChange(filters.copy(violenceType = v))
            }
        )

        // Bidang Layanan
        Text("Bidang Layanan", style = MaterialTheme.typography.titleSmall)
        FlowChipsRow(
            options = listOf("Psikologis", "Hukum", "Sosial", "Medis", "Shelter"),
            selectedIndex = when (filters.serviceType) {
                ServiceType.Psikologis -> 0
                ServiceType.Hukum -> 1
                ServiceType.Sosial -> 2
                ServiceType.Medis -> 3
                ServiceType.Shelter -> 4
                else -> -1
            },
            onSelected = {
                val s = when (it) {
                    0 -> ServiceType.Psikologis
                    1 -> ServiceType.Hukum
                    2 -> ServiceType.Sosial
                    3 -> ServiceType.Medis
                    else -> ServiceType.Shelter
                }
                onFiltersChange(filters.copy(serviceType = s))
            }
        )

        // Tombol Cari
        Button(
            onClick = {
                focus.clearFocus()
                onSubmit()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Cari Bantuan")
        }

        // Hint
        AssistChip(
            onClick = {},
            label = { Text("Tips: Isi semua kolom untuk hasil yang lebih akurat") },
            leadingIcon = {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// -----------------------------
// Hasil Rekomendasi (List)
// -----------------------------
@Composable
private fun RecommendationResultList(
    filters: RecommendationFilters,
    results: List<RecommendationCardUI>,
    onChangeFilter: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Ringkasan filter
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Hasil Rekomendasi", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Text(
                    text = buildString {
                        append("Provinsi: ${filters.province.ifBlank { "-" }} • ")
                        append("Layanan: ${filters.serviceType?.name ?: "-"} • ")
                        append("Jenis: ${filters.violenceType?.name ?: "-"}")
                    },
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = onChangeFilter) {
                    Text("Ubah Filter")
                }
            }
        }

        // Daftar kartu
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (results.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Tidak ada layanan yang cocok dengan filter.")
                    }
                }
            } else {
                items(results, key = { it.id }) { item ->
                    RecommendationCard(item)
                }
            }
        }
    }
}

@Composable
private fun RecommendationCard(item: RecommendationCardUI) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.serviceName,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(6.dp))
            AssistChip(
                onClick = {},
                label = { Text(item.serviceType.name) }
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalButton(onClick = { /* open link */ }) {
                    Icon(Icons.Default.Link, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Buka Link")
                }
                OutlinedButton(onClick = { /* call / share */ }) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Kontak")
                }
            }
        }
    }
}

// -----------------------------
// Util: Row of single-select FilterChips
// -----------------------------
@Composable
private fun FlowChipsRow(
    options: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit
) {
    // Simple responsive wrap using FlowRow substitute
    // (Tanpa dependency Accompanist; gunakan Rows berulang)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        var row = mutableListOf<Int>()
        // Render semua sebagai satu baris; jika mau wrap kompleks, gunakan FlowRow Accompanist.
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEachIndexed { index, label ->
                FilterChip(
                    selected = index == selectedIndex,
                    onClick = { onSelected(index) },
                    label = { Text(label) }
                )
            }
        }
    }
}

// -----------------------------
// Preview
// -----------------------------
@Preview(showBackground = true)
@Composable
private fun RecommendationFormPreview() {
    MaterialTheme {
        RecommendationScreen()
    }
}
