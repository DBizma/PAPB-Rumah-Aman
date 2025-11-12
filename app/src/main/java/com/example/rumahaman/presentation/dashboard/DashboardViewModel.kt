package com.example.rumahaman.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rumahaman.data.repository.TipsRepository
import com.example.rumahaman.presentation.notification.NotificationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val popularTips: List<NotificationItem.Tip> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val tipsRepository: TipsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadPopularTips()
    }

    fun loadPopularTips() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            tipsRepository.getPopularTips()
                .onSuccess { tips ->
                    _uiState.update {
                        it.copy(
                            popularTips = tips,
                            isLoading = false
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                }
        }
    }

    fun onTipClicked(tipId: String) {
        viewModelScope.launch {
            tipsRepository.incrementTipViewCount(tipId)
            // Reload tips to update counter
            loadPopularTips()
        }
    }
}
