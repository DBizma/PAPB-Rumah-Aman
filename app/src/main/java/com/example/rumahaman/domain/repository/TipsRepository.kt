package com.example.rumahaman.domain.repository

import com.example.rumahaman.core.utils.Resource
import com.example.rumahaman.domain.model.Tip
import kotlinx.coroutines.flow.Flow

interface TipsRepository {
    // Menggunakan Flow agar UI bisa mendapatkan update data secara real-time
    fun getAllTips(): Flow<Resource<List<Tip>>>
}
