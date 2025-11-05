package com.example.rumahaman.presentation.recommendation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

/* ====== Palet mirip Figma ====== */
private val Mint = Color(0xFFE8F3EF)
private val Teal = Color(0xFF2F7E83)
private val ChipSelected = Color(0xFFD9ECE7)
private val ChipBorder = Color(0xFFCCD6D2)
private val OrangeDot = Color(0xFFFFA76B)

/* ====== Model UI sederhana ====== */
enum class UiGender { Perempuan, LakiLaki }
enum class UiViolence { Fisik, Verbal, FisikDanVerbal }
enum class UiService { Psikologis, Hukum, PsikologisDanHukum }

data class RecFormState(
    val name: String = "Margaretha",
    val gender: UiGender? = UiGender.Perempuan,
    val age: String = "20",
    val province: String = "Jawa Timur",
    val violence: UiViolence? = UiViolence.Verbal,
    val service: UiService? = UiService.Psikologis
)

/* ====== Screen ====== */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationScreen(
    onBack: (() -> Unit)? = null,
    onSubmit: (RecFormState) -> Unit = {}
) {
    var state by remember { mutableStateOf(RecFormState()) }
    val provinces = listOf("Jawa Timur","Jawa Tengah","Jawa Barat","DKI Jakarta","DIY","Bali")
    var provinceExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            // Header ilustrasi + judul
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Mint),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Teal)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = (-6).dp)
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(OrangeDot)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "SISTEM REKOMENDASI",
                color = Teal,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(12.dp))

            // Kartu utama
            Surface(
                color = Mint,
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 0.dp,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Nama (read-only)
                    FieldLabel("Nama:")
                    OutlinedTextField(
                        value = state.name,
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Jenis Kelamin (2 kartu, tanpa weight)
                    FieldLabel("Jenis Kelamin:")
                    GenderRow(
                        selected = state.gender,
                        onSelect = { state = state.copy(gender = it) }
                    )

                    // Umur & Provinsi (dua kolom responsif, tanpa weight)
                    FieldLabel("Umur & Provinsi:")
                    AgeProvinceRow(
                        age = state.age,
                        province = state.province,
                        onAgeChange = { state = state.copy(age = it.filter(Char::isDigit).take(3)) },
                        onProvincePick = { provinceExpanded = true },
                        provinces = provinces,
                        expanded = provinceExpanded,
                        onExpandedChange = { provinceExpanded = it },
                        onProvinceSelected = {
                            state = state.copy(province = it)
                            provinceExpanded = false
                        }
                    )

                    // Jenis kekerasan
                    FieldLabel("Jenis kekerasan seksual yang didapatkan:")
                    PillRowFixed(
                        items = listOf("Fisik","Verbal","Fisik & Verbal"),
                        selectedIndex = when (state.violence) {
                            UiViolence.Fisik -> 0
                            UiViolence.Verbal -> 1
                            UiViolence.FisikDanVerbal -> 2
                            else -> -1
                        },
                        onSelected = { idx ->
                            state = state.copy(
                                violence = when (idx) {
                                    0 -> UiViolence.Fisik
                                    1 -> UiViolence.Verbal
                                    else -> UiViolence.FisikDanVerbal
                                }
                            )
                        }
                    )

                    // Bidang layanan
                    FieldLabel("Anda membutuhkan pelayanan di bidang:")
                    PillRowFixed(
                        items = listOf("Psikologi","Hukum","Psikologis & Hukum"),
                        selectedIndex = when (state.service) {
                            UiService.Psikologis -> 0
                            UiService.Hukum -> 1
                            UiService.PsikologisDanHukum -> 2
                            else -> -1
                        },
                        onSelected = { idx ->
                            state = state.copy(
                                service = when (idx) {
                                    0 -> UiService.Psikologis
                                    1 -> UiService.Hukum
                                    else -> UiService.PsikologisDanHukum
                                }
                            )
                        }
                    )

                    // Tombol submit
                    Button(
                        onClick = { onSubmit(state) },
                        colors = ButtonDefaults.buttonColors(containerColor = Teal),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) { Text("Tampilkan Rekomendasi") }
                }
            }
        }
    }
}

/* ====== Komponen kecil ====== */

@Composable
private fun FieldLabel(text: String) {
    Text(text, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
}

/** Dua kartu gender “lebar sama” tanpa weight (pakai BoxWithConstraints) */
@Composable
private fun GenderRow(
    selected: UiGender?,
    onSelect: (UiGender) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val spacing = 12.dp
        val cardW = (maxWidth - spacing) / 2

        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
            GenderCard(
                text = "Wanita",
                selected = selected == UiGender.Perempuan,
                onClick = { onSelect(UiGender.Perempuan) },
                modifier = Modifier.width(cardW)
            )
            GenderCard(
                text = "Pria",
                selected = selected == UiGender.LakiLaki,
                onClick = { onSelect(UiGender.LakiLaki) },
                modifier = Modifier.width(cardW)
            )
        }
    }
}

@Composable
private fun GenderCard(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (selected) Color.White else Color(0xFFF2F5F4)
    val border = if (selected) Teal.copy(alpha = .35f) else ChipBorder
    Column(
        modifier = modifier
            .height(104.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color(0xFFDDEBE8)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = Teal)
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text,
            color = if (selected) Teal else Color.Black.copy(alpha = .75f),
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

/** Dua kolom “Umur & Provinsi” tanpa weight; province pakai ExposedDropdown */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AgeProvinceRow(
    age: String,
    province: String,
    onAgeChange: (String) -> Unit,
    onProvincePick: () -> Unit,
    provinces: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onProvinceSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val spacing = 12.dp
        val ageW = (maxWidth - spacing) * 0.33f
        val provW = maxWidth - spacing - ageW

        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
            Column(Modifier.width(ageW)) {
                OutlinedTextField(
                    value = age,
                    onValueChange = { onAgeChange(it) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(Modifier.width(provW)) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = onExpandedChange
                ) {
                    OutlinedTextField(
                        value = province,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { onExpandedChange(false) }
                    ) {
                        provinces.forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p) },
                                onClick = { onProvinceSelected(p) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/** Tiga pill fixed width, tanpa weight */
@Composable
private fun PillRowFixed(
    items: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val spacing = 12.dp
        val pillW = (maxWidth - spacing * (items.size - 1)) / items.size

        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
            items.forEachIndexed { i, label ->
                val selected = i == selectedIndex
                Box(
                    modifier = Modifier
                        .width(pillW)
                        .height(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selected) ChipSelected else Color.White)
                        .border(1.dp, ChipBorder, RoundedCornerShape(12.dp))
                        .clickable { onSelected(i) }
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        label,
                        color = if (selected) Teal else Color.Black.copy(alpha = .80f),
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecommendationScreenPreview() {
    MaterialTheme { RecommendationScreen() }
}
