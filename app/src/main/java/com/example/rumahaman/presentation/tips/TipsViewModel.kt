package com.example.rumahaman.presentation.tips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rumahaman.core.utils.Resource
import com.example.rumahaman.domain.usecase.tips.GetAllTipsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TipsViewModel @Inject constructor(
    private val getAllTipsUseCase: GetAllTipsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TipsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTips()
    }

    private fun getTips() {
        getAllTipsUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            tips = result.data ?: emptyList()
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}
