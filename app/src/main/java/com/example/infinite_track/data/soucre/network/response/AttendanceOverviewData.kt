package com.example.infinite_track.data.soucre.network.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AttendanceOverviewData(
    val title: String,
    val count: Int,
    val unit: String
): Parcelable
