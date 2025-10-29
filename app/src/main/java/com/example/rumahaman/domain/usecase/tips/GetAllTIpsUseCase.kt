package com.example.rumahaman.domain.usecase.tips

import com.example.rumahaman.domain.repository.TipsRepository
import javax.inject.Inject

class GetAllTipsUseCase @Inject constructor(
    private val repository: TipsRepository
) {
    // 'operator fun invoke' memungkinkan kita memanggil kelas ini seolah-olah fungsi
    operator fun invoke() = repository.getAllTips()
}
