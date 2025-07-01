package com.example.infinite_track.data.soucre.network.request

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the request body for updating a user profile.
 * All fields are nullable to support PATCH operations, where only changed fields are sent.
 */
data class ProfileUpdateRequest(
    @SerializedName("full_name") val fullName: String? = null,
    @SerializedName("nip_nim") val nipNim: String? = null,
    @SerializedName("phone") val phone: String? = null
)
