package com.example.infinite_track.data.soucre.network.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("code")
    val code: String? = null
)
