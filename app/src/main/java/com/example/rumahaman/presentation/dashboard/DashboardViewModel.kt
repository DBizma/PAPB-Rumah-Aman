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
    val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val tipsRepository: TipsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        // Listen to real-time updates
        viewModelScope.launch {
            tipsRepository.getPopularTipsStream()
                .collect { tips ->
                    _uiState.update {
                        it.copy(
                            popularTips = tips,
                            error = null
                        )
                    }
                }
        }
    }

    fun onTipClicked(tipId: String) {
        viewModelScope.launch {
            tipsRepository.incrementTipViewCount(tipId)
            // No need to reload, real-time listener will update automatically
        }
    }
}
