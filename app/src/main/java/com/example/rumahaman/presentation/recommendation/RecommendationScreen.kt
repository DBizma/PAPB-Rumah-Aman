package com.example.rumahaman.presentation.recommendation

import com.example.rumahaman.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TealPrimary = Color(0xFF2E7D84)
private val FormBackground = Color(0x57AAE2DB)
private val FieldBackground = Color(0xFFD9E6E2)

private enum class Gender { Female, Male }
private enum class ViolenceType { Physical, Verbal, Both }
private enum class ServiceType { Psychology, Law, Both }

@Composable
fun RecommendationScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onShowRecommendationClick: () -> Unit = {}
) {
    var selectedGender by remember { mutableStateOf(Gender.Female) }
    var selectedViolence by remember { mutableStateOf(ViolenceType.Verbal) }
    var selectedService by remember { mutableStateOf(ServiceType.Psychology) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // background hijau dengan rounded top-left (sheet)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 253.dp)
                .clip(RoundedCornerShape(topStart = 70.dp))
                .background(FormBackground)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali"
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "19:02",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            HeaderSection()

            Spacer(modifier = Modifier.height(24.dp))

            // Nama
            Text(
                text = "Nama:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.32).sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            FieldBox(
                text = "Margaretha",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Jenis Kelamin
            Text(
                text = "Jenis Kelamin:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.32).sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GenderCard(
                    label = "Wanita",
                    imageRes = R.drawable.perempuan_sistemrekomendasi,
                    selected = selectedGender == Gender.Female,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedGender = Gender.Female }
                )
                GenderCard(
                    label = "Pria",
                    imageRes = R.drawable.laki2_sistemrekomendasi,
                    selected = selectedGender == Gender.Male,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedGender = Gender.Male }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Umur & Provinsi
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Umur:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.32).sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FieldBox(
                        text = "20",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(
                    modifier = Modifier.weight(2f)
                ) {
                    Text(
                        text = "Provinsi:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.32).sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FieldBox(
                        text = "Jawa Timur",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Jenis kekerasan
            Text(
                text = "Jenis kekerasan seksual yang didapatkan:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.32).sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SelectableChip(
                    text = "Fisik",
                    selected = selectedViolence == ViolenceType.Physical,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedViolence = ViolenceType.Physical }
                )
                SelectableChip(
                    text = "Verbal",
                    selected = selectedViolence == ViolenceType.Verbal,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedViolence = ViolenceType.Verbal }
                )
                SelectableChip(
                    text = "Fisik & Verbal",
                    selected = selectedViolence == ViolenceType.Both,
                    modifier = Modifier.weight(1.2f),
                    onClick = { selectedViolence = ViolenceType.Both }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pelayanan
            Text(
                text = "Anda membutuhkan pelayanan di bidang:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.32).sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SelectableChip(
                    text = "Psikologi",
                    selected = selectedService == ServiceType.Psychology,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedService = ServiceType.Psychology }
                )
                SelectableChip(
                    text = "Hukum",
                    selected = selectedService == ServiceType.Law,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedService = ServiceType.Law }
                )
                SelectableChip(
                    text = "Psikologis & Hukum",
                    selected = selectedService == ServiceType.Both,
                    modifier = Modifier.weight(1.4f),
                    onClick = { selectedService = ServiceType.Both }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onShowRecommendationClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(47.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TealPrimary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Tampilkan Rekomendasi",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    letterSpacing = (-0.36).sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        // lingkaran besar hijau muda
        Box(
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
                .background(Color(0xFFE5F3EF)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.robot_sistemrekomendasi),
                contentDescription = null,
                modifier = Modifier.size(110.dp)
            )
        }

        // titik kecil oranye di kanan atas (169-757)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-24).dp, y = 12.dp)
                .size(18.dp)
                .clip(CircleShape)
                .background(Color(0xFFFF9E80))
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(top = 140.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SISTEM REKOMENDASI",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TealPrimary,
                textAlign = TextAlign.Center,
                letterSpacing = (-0.36).sp
            )
        }
    }
}

@Composable
private fun FieldBox(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(FieldBackground)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun GenderCard(
    label: String,
    imageRes: Int,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = FieldBackground,
        border = if (selected) {
            BorderStroke(2.dp, Color.Black.copy(alpha = 0.2f))
        } else {
            null
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (selected) TealPrimary else Color.Black,
                textAlign = TextAlign.Center
            )
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = label,
                modifier = Modifier.size(70.dp)
            )
        }
    }
}

@Composable
private fun SelectableChip(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val borderModifier = if (selected) {
        Modifier.border(
            width = 2.dp,
            color = Color.Black.copy(alpha = 0.2f),
            shape = RoundedCornerShape(10.dp)
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(FieldBackground)
            .then(borderModifier)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (selected) TealPrimary else Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RecommendationScreenPreview() {
    MaterialTheme {
        RecommendationScreen()
    }
}
