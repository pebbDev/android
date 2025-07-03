package com.example.infinite_track.data.soucre.network.response

import com.google.gson.annotations.SerializedName

/**
 * Network response models for WFA (Work From Anywhere) recommendations API
 */
data class WfaRecommendationResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: WfaData
)

data class WfaData(
    @SerializedName("user_location")
    val userLocation: UserLocation,
    @SerializedName("recommendations")
    val recommendations: List<RecommendationItem>
)

data class UserLocation(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("address")
    val address: String?
)

data class RecommendationItem(
    @SerializedName("name")
    val name: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("category")
    val category: String,
    @SerializedName("suitability_score")
    val suitabilityScore: Double,
    @SerializedName("suitability_label")
    val suitabilityLabel: String,
    @SerializedName("distance_from_center")
    val distanceFromCenter: Double, // This is in meters from API
    @SerializedName("score_details")
    val scoreDetails: ScoreDetails
)

data class ScoreDetails(
    @SerializedName("wifi_quality")
    val wifiQuality: WifiQuality,
    @SerializedName("noise_level")
    val noiseLevel: NoiseLevel,
    @SerializedName("crowd_density")
    val crowdDensity: CrowdDensity,
    @SerializedName("operational_hours")
    val operationalHours: OperationalHours,
    @SerializedName("amenities")
    val amenities: Amenities
)

data class WifiQuality(
    @SerializedName("score")
    val score: Double,
    @SerializedName("label")
    val label: String,
    @SerializedName("description")
    val description: String
)

data class NoiseLevel(
    @SerializedName("score")
    val score: Double,
    @SerializedName("label")
    val label: String,
    @SerializedName("description")
    val description: String
)

data class CrowdDensity(
    @SerializedName("score")
    val score: Double,
    @SerializedName("label")
    val label: String,
    @SerializedName("description")
    val description: String
)

data class OperationalHours(
    @SerializedName("score")
    val score: Double,
    @SerializedName("label")
    val label: String,
    @SerializedName("description")
    val description: String
)

data class Amenities(
    @SerializedName("score")
    val score: Double,
    @SerializedName("label")
    val label: String,
    @SerializedName("facilities")
    val facilities: List<String>
)
