package com.example.infinite_track.domain.model.wfa

/**
 * Domain models for WFA (Work From Anywhere) recommendations
 * These are clean models used by the UI layer
 */
data class WfaRecommendation(
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val score: Double,
    val label: String,
    val category: String,
    val distance: Double
)

/**
 * Detailed WFA recommendation with score breakdown
 */
data class WfaRecommendationDetail(
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val category: String,
    val suitabilityScore: Double,
    val suitabilityLabel: String,
    val distanceKm: Double,
    val wifiQuality: ScoreItem,
    val noiseLevel: ScoreItem,
    val crowdDensity: ScoreItem,
    val operationalHours: ScoreItem,
    val amenities: AmenityItem
)

data class ScoreItem(
    val score: Double,
    val label: String,
    val description: String
)

data class AmenityItem(
    val score: Double,
    val label: String,
    val facilities: List<String>
)
