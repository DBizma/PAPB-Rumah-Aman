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
    @PropertyName("logo_url") val logoUrl: String? = null,
    @PropertyName("tags") val tags: List<String> = emptyList(),
    @PropertyName("last_updated") val lastUpdated: String = ""
)

data class RecommendationResult(
    @PropertyName("recommendation_id") val recommendationId: String = "",
    @PropertyName("input") val input: RecommendationInput = RecommendationInput(),
    @PropertyName("service") val service: RecommendationService = RecommendationService()
)
