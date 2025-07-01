package com.example.infinite_track.domain.model

data class TimeOffModel(
    val id: Int,
    val name: String,
    val division: String,
    val leaveStartDate: String,
    val leaveEndDate: String,
    val submittedAt: String,
    val photoProfile: String?
)

