package com.example.rumahaman.presentation.recommendation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rumahaman.data.model.RecommendationResult
import com.example.rumahaman.data.repository.RecommendationRepository
import com.example.rumahaman.data.repository.Result
import com.example.rumahaman.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
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
    val gender: String = "",
    val age: String = "",
    val province: String = "",
    val violenceType: String = "",
    val serviceType: String = ""
)

@HiltViewModel
class RecommendationViewModel @Inject constructor(
    private val repository: RecommendationRepository,
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecommendationUiState())
    val uiState: StateFlow<RecommendationUiState> = _uiState.asStateFlow()
    
    init {
        loadUserData()
    }
    
    private fun loadUserData() {
        val userId = auth.currentUser?.uid ?: return
        
        viewModelScope.launch {
            userRepository.getUserData(userId).collect { result ->
                when (result) {
                    is Result.Success -> {
                        val user = result.data
                        _uiState.value = _uiState.value.copy(
                            name = user.name,
                            gender = user.gender,
                            age = if (user.age > 0) user.age.toString() else "",
                            province = user.province
                        )
                    }
                    is Result.Error -> {
                        // Keep default values if error
                    }
                    is Result.Loading -> {
                        // Keep loading
                    }
                }
            }
        }
    }
    
    // Remove updateName function - name is read-only from user data

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
            
            // Log input parameters
            android.util.Log.d("RecommendationViewModel", "Getting recommendation with:")
            android.util.Log.d("RecommendationViewModel", "  Name: ${_uiState.value.name}")
            android.util.Log.d("RecommendationViewModel", "  Gender: ${_uiState.value.gender}")
            android.util.Log.d("RecommendationViewModel", "  Age: ${_uiState.value.age}")
            android.util.Log.d("RecommendationViewModel", "  Province: ${_uiState.value.province}")
            android.util.Log.d("RecommendationViewModel", "  Violence Type: ${_uiState.value.violenceType}")
            android.util.Log.d("RecommendationViewModel", "  Service Type: ${_uiState.value.serviceType}")
            
            val result = repository.getRecommendation(
                gender = _uiState.value.gender,
                violence = _uiState.value.violenceType,
                need = _uiState.value.serviceType,
                province = _uiState.value.province
            )

            result.fold(
                onSuccess = { recommendation ->
                    android.util.Log.d("RecommendationViewModel", "✅ Success: Got recommendation ${recommendation.service.name}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        recommendation = recommendation,
                        error = null
                    )
                },
                onFailure = { exception ->
                    android.util.Log.e("RecommendationViewModel", "❌ Failed: ${exception.message}", exception)
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
