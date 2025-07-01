package com.example.infinite_track.data.soucre.network.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class LeaveHistoryResponse(

	@field:SerializedName("data")
	val dataLeave: List<DataLeave?>? = null
) : Parcelable

@Parcelize
data class DataLeave(

	@field:SerializedName("leaveId")
	val leaveId: Int? = null,

	@field:SerializedName("userName")
	val userName: String? = null,

	@field:SerializedName("profile_photo")
	val profilePhoto: String? = null,

	@field:SerializedName("division")
	val division: String? = null,

	@field:SerializedName("start_date")
	val startDate: String? = null,

	@field:SerializedName("end_date")
	val endDate: String? = null,

	@field:SerializedName("submitted_at")
	val submittedAt: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("upload_image")
	val uploadImage: String? = null,

	@field:SerializedName("userId")
	val userId: Int? = null,
) : Parcelable
