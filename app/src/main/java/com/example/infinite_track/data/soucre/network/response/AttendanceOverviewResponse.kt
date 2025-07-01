package com.example.infinite_track.data.soucre.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AttendanceOverviewResponse(
    @field:SerializedName("overview")
    val overviewData: OverviewData? = null
) : Parcelable


@Parcelize
data class OverviewData(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("check_in_time")
    val checkinTime: String? = null,

    @field:SerializedName("check_out_time")
    val checkoutTime: String? = null,

    @field:SerializedName("late")
    val late: String? = null,

    @field:SerializedName("on_time")
    val onTime: String? = null,

    @field:SerializedName("total_absence")
    val totalAbsence: String? = null,

    @field:SerializedName("total_attendance")
    val totalAttendance: Int? = null,

    @field:SerializedName("total_work_from_home")
    val totalWFH: String? = null,

    @field:SerializedName("total_work_from_office")
    val totalWFO: String? = null,
) : Parcelable
