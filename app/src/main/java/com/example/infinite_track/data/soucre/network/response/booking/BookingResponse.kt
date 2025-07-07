package com.example.infinite_track.data.soucre.network.response.booking

import com.google.gson.annotations.SerializedName

data class BookingResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: BookingResultData?
)

data class BookingResultData(
    @SerializedName("booking_id")
    val bookingId: Int,

    @SerializedName("status")
    val status: String,

    @SerializedName("created_at")
    val createdAt: String
)
