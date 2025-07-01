package com.example.infinite_track.data.soucre.network.request

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class AttendanceRequest(
	@field:SerializedName("category_id")
	val categoryId: Int,  // 1=WFO, 2=WFH, 3=WFA

	@field:SerializedName("notes")
	val notes: String,

	@field:SerializedName("latitude")
	val latitude: Double,

	@field:SerializedName("longitude")
	val longitude: Double,

	@field:SerializedName("booking_id")
	val bookingId: Int? = null  // Required only for WFA
) : Parcelable
