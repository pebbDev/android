package com.example.infinite_track.domain.model.auth

/**
 * Domain model for User that contains all essential data from API response
 * This ensures no data loss when caching or retrieving from local storage
 */
data class UserModel(
    val id: Int,
    val fullName: String,
    val email: String,
    val roleName: String,
    val positionName: String?,
    val programName: String?,
    val divisionName: String?,
    val nipNim: String,
    val phone: String?,
    val photoUrl: String?,
    val photoUpdatedAt: String?,
    // Location data
    val latitude: Double?,
    val longitude: Double?,
    val radius: Int?,
    val locationDescription: String?,
    val locationCategoryName: String?,
    // Optional face embedding data
    val faceEmbedding: ByteArray? = null
)