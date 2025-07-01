package com.example.infinite_track.data.soucre.network.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class AttendanceResponse(
	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("data")
	val data: AttendanceData
) : Parcelable

@Parcelize
data class AttendanceData(
	@field:SerializedName("id_attendance")
	val idAttendance: Int,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("category_id")
	val categoryId: Int,

	@field:SerializedName("status_id")
	val statusId: Int,

	@field:SerializedName("location_id")
	val locationId: Int,

	@field:SerializedName("booking_id")
	val bookingId: Int? = null,

	@field:SerializedName("time_in")
	val timeIn: String,

	@field:SerializedName("time_out")
	val timeOut: String? = null,

	@field:SerializedName("work_hour")
	val workHour: String,

	@field:SerializedName("attendance_date")
	val attendanceDate: String,

	@field:SerializedName("notes")
	val notes: String,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null
) : Parcelable
