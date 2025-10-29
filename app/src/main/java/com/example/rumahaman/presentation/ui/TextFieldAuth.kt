package com.example.rumahaman.presentation.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rumahaman.presentation.ui.theme.LightGreenGray
import com.example.rumahaman.presentation.ui.theme.RumahAmanTheme
import com.example.rumahaman.presentation.ui.theme.TealColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldAuth(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isValid: Boolean?,
    isPassword: Boolean = false,
    // 1. Tambahkan parameter baru untuk mengontrol ikon mata
    showPasswordToggle: Boolean = false
) {
    // State untuk mengontrol visibilitas password, hanya relevan jika isPassword true
    var isPasswordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(placeholder) },
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        // 2. Tentukan visual transformation berdasarkan isPassword dan isPasswordVisible
        visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = LightGreenGray,
            unfocusedContainerColor = LightGreenGray,
            disabledContainerColor = LightGreenGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = TealColor
        ),
        trailingIcon = {
            // 3. Logika baru untuk trailingIcon
            if (showPasswordToggle) {
                // Jika diminta untuk menampilkan toggle, prioritaskan itu
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    val icon = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    val description = if (isPasswordVisible) "Sembunyikan password" else "Tampilkan password"
                    Icon(imageVector = icon, contentDescription = description)
                }
            } else if (isValid != null) {
                // Jika tidak ada toggle, baru tampilkan ikon validasi
                val icon = if (isValid) Icons.Default.Check else Icons.Default.Close
                val description = if (isValid) "Input Valid" else "Input Tidak Valid"
                val tint = if (isValid) TealColor else MaterialTheme.colorScheme.error

                Icon(
                    imageVector = icon,
                    contentDescription = description,
                    tint = tint
                )
            }
        }
    )
}



// --- Previews untuk melihat tampilan komponen ---

@Preview(showBackground = true, name = "Valid Input")
@Composable
private fun TextFieldAuthValidPreview() {
    RumahAmanTheme {
        TextFieldAuth(
            value = "margaretha_",
            onValueChange = {},
            placeholder = "Nama",
            isValid = true
        )
    }
}

// Preview baru untuk password dengan toggle
@Preview(showBackground = true, name = "Password with Toggle")
@Composable
private fun TextFieldAuthPasswordWithTogglePreview() {
    RumahAmanTheme {
        TextFieldAuth(
            value = "password123",
            onValueChange = {},
            placeholder = "Kata Sandi",
            isValid = true, // isValid akan diabaikan karena showPasswordToggle = true
            isPassword = true,
            showPasswordToggle = true
        )
    }
}

// Preview untuk konfirmasi password tanpa toggle
@Preview(showBackground = true, name = "Confirm Password without Toggle")
@Composable
private fun TextFieldAuthConfirmPasswordPreview() {
    RumahAmanTheme {
        TextFieldAuth(
            value = "password123",
            onValueChange = {},
            placeholder = "Ulangi Kata Sandi",
            isValid = true, // isValid akan ditampilkan karena showPasswordToggle = false
            isPassword = true,
            showPasswordToggle = false // Tidak menampilkan ikon mata
        )
    }
}

@Preview(showBackground = true, name = "Neutral Input (No Icon)")
@Composable
private fun TextFieldAuthNeutralPreview() {
    RumahAmanTheme {
        TextFieldAuth(
            value = "",
            onValueChange = {},
            placeholder = "Email",
            isValid = null
        )
    }
}

