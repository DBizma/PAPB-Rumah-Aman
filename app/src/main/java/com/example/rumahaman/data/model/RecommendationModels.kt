package com.example.rumahaman.data.model

import com.google.firebase.firestore.PropertyName

data class RecommendationInput(
    @PropertyName("gender") val gender: String = "",
    @PropertyName("violence") val violence: String = "",
    @PropertyName("need") val need: String = "",
    @PropertyName("province") val province: String = ""
)

data class ServiceContact(
    @PropertyName("url") val url: String = "",
    @PropertyName("phone") val phone: String = "",
    @PropertyName("email") val email: String = ""
)

data class RecommendationService(
    @PropertyName("name") val name: String = "",
    @PropertyName("type") val type: String = "",
    @PropertyName("description") val description: String = "",
    @PropertyName("address") val address: String = "",
    @PropertyName("contact") val contact: ServiceContact = ServiceContact(),
    @PropertyName("logoUrl") val logoUrl: String? = null,  // Changed from logo_url
    @PropertyName("tags") val tags: List<String> = emptyList(),
    @PropertyName("lastUpdated") val lastUpdated: String = ""  // Changed from last_updated
)

data class RecommendationResult(
    @PropertyName("recommendationId") val recommendationId: String = "",  // Changed from recommendation_id
    @PropertyName("input") val input: RecommendationInput = RecommendationInput(),
    @PropertyName("service") val service: RecommendationService = RecommendationService()
)
