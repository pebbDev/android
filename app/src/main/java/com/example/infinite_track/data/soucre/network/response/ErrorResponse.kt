package com.example.infinite_track.data.soucre.network.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @field:SerializedName("message")
    val message: String? = null
)