package com.example.infinite_track.data.soucre.network.request

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for check-out request
 * Used as request body when calling check-out API endpoint
 */
data class CheckOutRequestDto(
    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double
)
