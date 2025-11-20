package com.example.rumahaman.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rumahaman.data.repository.Result
import com.example.rumahaman.data.repository.TipsRepository
import com.example.rumahaman.domain.model.User
import com.example.rumahaman.domain.repository.UserRepository
import com.example.rumahaman.presentation.notification.NotificationItem
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val userName: String = "User",
    val popularTips: List<NotificationItem.Tip> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val tipsRepository: TipsRepository,
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadUserName()
        
        // Listen to real-time updates for tips
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
    
    private fun loadUserName() {
        val userId = auth.currentUser?.uid ?: return
        
        viewModelScope.launch {
            userRepository.getUserData(userId).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(userName = result.data.name.ifEmpty { "User" })
                        }
                    }
                    is Result.Error -> {
                        // Fallback ke displayName dari Firebase Auth
                        val fallbackName = auth.currentUser?.displayName ?: "User"
                        _uiState.update {
                            it.copy(userName = fallbackName)
                        }
                    }
                    is Result.Loading -> {
                        // Keep current name while loading
                    }
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
    
    fun refreshUserName() {
        loadUserName()
    }
}
