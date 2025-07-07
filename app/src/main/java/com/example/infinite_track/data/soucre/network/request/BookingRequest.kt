package com.example.infinite_track.data.soucre.network.request

import com.google.gson.annotations.SerializedName

data class BookingRequest(
    @SerializedName("schedule_date")
    val scheduleDate: String,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,

    @SerializedName("radius")
    val radius: Int,

    @SerializedName("description")
    val description: String,

    @SerializedName("notes")
    val notes: String
)
