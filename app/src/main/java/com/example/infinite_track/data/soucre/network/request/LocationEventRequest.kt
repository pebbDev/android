package com.example.infinite_track.data.soucre.network.request

import com.google.gson.annotations.SerializedName

/**
 * Request DTO untuk mengirim location event ke backend
 */
data class LocationEventRequest(
    @SerializedName("event_type")
    val eventType: String, // "ENTER" atau "EXIT"

    @SerializedName("location_id")
    val locationId: Int, // ID lokasi dari geofence

    @SerializedName("event_timestamp")
    val eventTimestamp: String // ISO timestamp format
)
