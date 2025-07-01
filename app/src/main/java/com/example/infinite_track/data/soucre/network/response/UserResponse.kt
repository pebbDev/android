package com.example.infinite_track.data.soucre.network.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class UserResponse(

	@field:SerializedName("annualBalance")
	val annualBalance: Int? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("start_contract")
	val startContract: String? = null,

	@field:SerializedName("greeting")
	val greeting: String,

	@field:SerializedName("annualUsed")
	val annualUsed: Int? = null,

	@field:SerializedName("userName")
	val userName: String,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("userId")
	val userId: Int? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("division")
	val division: String? = null,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("positionName")
	val positionName: String,

	@field:SerializedName("end_contract")
	val endContract: String? = null,

	@field:SerializedName("headprogramname")
	val headprogramname: String? = null,

	@field:SerializedName("nip_nim")
	val nipNim: String? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("userRole")
	val userRole: String,

	@field:SerializedName("isProfileComplete")
	val isProfileComplete: String? = null,

	@field:SerializedName("profilePhoto")
	val profilePhoto: String? = null

) : Parcelable


