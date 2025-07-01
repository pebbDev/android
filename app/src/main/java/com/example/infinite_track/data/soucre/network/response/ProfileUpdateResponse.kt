package com.example.infinite_track.data.soucre.network.response

import com.google.gson.annotations.SerializedName

/**
 * Response model for profile update API
 */
data class ProfileUpdateResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: UserData
)
