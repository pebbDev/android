package com.example.infinite_track.data.soucre.network.response.booking

import com.google.gson.annotations.SerializedName

data class BookingHistoryResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: BookingData
)

data class BookingData(
    @SerializedName("bookings")
    val bookings: List<BookingItem>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("limit")
    val limit: Int
)

data class BookingItem(
    @SerializedName("id")
    val id: String?, // Make nullable
    @SerializedName("location")
    val location: BookingLocation?, // Make nullable
    @SerializedName("schedule_date")
    val scheduleDate: String?, // Make nullable
    @SerializedName("status")
    val status: String?, // Make nullable
    @SerializedName("suitability_score")
    val suitabilityScore: Float?, // Make nullable
    @SerializedName("notes")
    val notes: String?, // Make nullable
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)

data class BookingLocation(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("radius")
    val radius: Int
)
