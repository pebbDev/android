package com.example.infinite_track.domain.model.dashboard

/**
 * Domain model representing summary data for the Internship Dashboard
 * Clean and free from external dependencies
 */
data class InternshipSummary(
    val checkedInTime: String?,
    val checkedOutTime: String?,
    val totalAbsence: Int,
    val totalAttended: Int
)
