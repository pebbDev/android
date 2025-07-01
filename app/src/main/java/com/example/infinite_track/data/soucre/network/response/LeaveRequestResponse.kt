package com.example.infinite_track.data.soucre.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class LeaveRequestResponse(
    @field:SerializedName("message")
    val message: String? = null,
) : Parcelable