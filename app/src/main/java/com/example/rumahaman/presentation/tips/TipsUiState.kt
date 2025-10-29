package com.example.rumahaman.presentation.tips

import com.example.rumahaman.domain.model.Tip

// Merepresentasikan semua state yang dibutuhkan oleh layar Tips
data class TipsUiState(
    val isLoading: Boolean = false,
    val tips: List<Tip> = emptyList(),
    val errorMessage: String? = null
)
