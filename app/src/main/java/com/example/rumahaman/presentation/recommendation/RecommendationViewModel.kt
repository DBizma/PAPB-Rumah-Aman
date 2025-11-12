package com.example.rumahaman.presentation.recommendation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rumahaman.data.model.RecommendationResult
import com.example.rumahaman.data.repository.RecommendationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecommendationUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val recommendation: RecommendationResult? = null,
    val name: String = "",
    val gender: String = "Perempuan",
    val age: String = "",
    val province: String = "",
    val violenceType: String = "VERBAL",
    val serviceType: String = "PSIKOLOGI"
)

@HiltViewModel
class RecommendationViewModel @Inject constructor(
    private val repository: RecommendationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecommendationUiState())
    val uiState: StateFlow<RecommendationUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun updateGender(gender: String) {
        _uiState.value = _uiState.value.copy(gender = gender)
    }

    fun updateAge(age: String) {
        _uiState.value = _uiState.value.copy(age = age)
    }

    fun updateProvince(province: String) {
        _uiState.value = _uiState.value.copy(province = province)
    }

    fun updateViolenceType(type: String) {
        _uiState.value = _uiState.value.copy(violenceType = type)
    }

    fun updateServiceType(type: String) {
        _uiState.value = _uiState.value.copy(serviceType = type)
    }

    fun getRecommendation() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val result = repository.getRecommendation(
                gender = _uiState.value.gender,
                violence = _uiState.value.violenceType,
                need = _uiState.value.serviceType,
                province = _uiState.value.province
            )

            result.fold(
                onSuccess = { recommendation ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        recommendation = recommendation,
                        error = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Terjadi kesalahan"
                    )
                }
            )
        }
    }

    fun clearRecommendation() {
        _uiState.value = _uiState.value.copy(recommendation = null, error = null)
    }
}
