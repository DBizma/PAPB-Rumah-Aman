package com.example.rumahaman.presentation.recommendation



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

/* ================================
   THEME (Material 3)
   ================================ */

private val LightColors = lightColorScheme(
    primary = Color(0xFF2F8C80),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBFE3DB),
    onPrimaryContainer = Color(0xFF08342F),

    secondary = Color(0xFF6AAE9E),
    onSecondary = Color.White,

    background = Color(0xFFF6F7F7),
    onBackground = Color(0xFF1B1B1B),

    surface = Color(0xFFF6F7F7),
    onSurface = Color(0xFF1B1B1B),

    surfaceContainerLow = Color(0xFFE7F3F0), // area form hijau muda
)

@Composable
fun RecommendationTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography(),
        content = content
    )
}

/* ================================
   DATA MODEL
   ================================ */
enum class Gender { Female, Male }

data class FormState(
    val name: String,
    val age: String,
    val province: String,
    val gender: Gender?,
    val violenceType: String,
    val services: List<String>
)

/* ================================
   SCREEN & COMPONENTS
   ================================ */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationFormScreen(
    onBack: () -> Unit = {},
    onSubmit: (FormState) -> Unit = {}
) {
    var name by remember { mutableStateOf("Margaretha") }
    var age by remember { mutableStateOf("20") }
    var province by remember { mutableStateOf("Jawa Timur") }

    var gender by remember { mutableStateOf<Gender?>(Gender.Female) }

    val violenceOptions = listOf("Fisik", "Verbal", "Fisik & Verbal")
    var selectedViolence by remember { mutableStateOf("Verbal") } // single-select

    val serviceOptions = listOf("Psikologi", "Hukum", "Psikologis & Hukum")
    var selectedServices by remember { mutableStateOf(setOf("Psikologi")) } // multi-select

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {

            // Header
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(contentAlignment = Alignment.TopEnd) {
                    Box(
                        Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .shadow(6.dp, CircleShape, spotColor = Color(0x33000000))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // TODO: Ganti dengan aset Anda
                        Image(
                            painter = painterResource(id = R.drawable.robot_sistemrekomendasi.png),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Box(
                        Modifier
                            .size(14.dp)
                            .offset(x = 10.dp, y = 6.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary)
                    )
                }
            }
            Text(
                "SISTEM REKOMENDASI",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color(0xFF5F6967),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.8.sp
            )

            Spacer(Modifier.height(12.dp))

            // Container form (sudut kiri atas besar)
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            topStart = 100.dp, topEnd = 16.dp,
                            bottomEnd = 16.dp, bottomStart = 16.dp
                        )
                    )
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                    // Nama
                    LabeledField(label = "Nama:") {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Jenis Kelamin
                    Text("Jenis Kelamin:", fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        GenderCard(
                            title = "Wanita",
                            imageRes = R.drawable.ic_female_avatar,
                            selected = gender == Gender.Female
                        ) { gender = Gender.Female }
                        GenderCard(
                            title = "Pria",
                            imageRes = R.drawable.ic_male_avatar,
                            selected = gender == Gender.Male
                        ) { gender = Gender.Male }
                    }

                    // Umur & Provinsi
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        LabeledField(label = "Umur:", modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = age,
                                onValueChange = { age = it.filter(Char::isDigit) },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                        LabeledField(label = "Provinsi:", modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = province,
                                onValueChange = { province = it },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }

                    // Jenis kekerasan (single-select)
                    Text("Jenis kekerasan seksual yang didapatkan:", fontWeight = FontWeight.SemiBold)
                    FlowChipsSingle(
                        options = violenceOptions,
                        selected = selectedViolence,
                        onSelected = { selectedViolence = it }
                    )

                    // Kebutuhan layanan (multi-select)
                    Text("Anda membutuhkan pelayanan di bidang:", fontWeight = FontWeight.SemiBold)
                    FlowChipsMulti(
                        options = serviceOptions,
                        selected = selectedServices,
                        onSelectedChange = { selectedServices = it }
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            // CTA
            Button(
                onClick = {
                    onSubmit(
                        FormState(
                            name = name,
                            age = age,
                            province = province,
                            gender = gender,
                            violenceType = selectedViolence,
                            services = selectedServices.toList()
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Tampilkan Rekomendasi", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun LabeledField(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, fontWeight = FontWeight.SemiBold)
        content()
    }
}

@Composable
private fun GenderCard(
    title: String,
    imageRes: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else Color(0x33000000)
    val container = if (selected) Color.White else Color(0xFFF2F2F2)

    Column(
        Modifier
            .width(140.dp)
            .clip(shape)
            .background(container)
            .shadow(if (selected) 6.dp else 2.dp, shape)
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            modifier = Modifier
                .fillMaxWidth()
                .height(92.dp)
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(8.dp))
        Text(
            title,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun FlowChipsSingle(
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        options.forEach { label ->
            FilterChip(
                selected = selected == label,
                onClick = { onSelected(label) },
                label = { Text(label) },
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
private fun FlowChipsMulti(
    options: List<String>,
    selected: Set<String>,
    onSelectedChange: (Set<String>) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        options.forEach { label ->
            val isSelected = label in selected
            FilterChip(
                selected = isSelected,
                onClick = {
                    onSelectedChange(if (isSelected) selected - label else selected + label)
                },
                label = { Text(label) },
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

/* ================================
   PREVIEW
   ================================ */

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RecommendationPreview() {
    RecommendationTheme {
        RecommendationFormScreen()
    }
}

