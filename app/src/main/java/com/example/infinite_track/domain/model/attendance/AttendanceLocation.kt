package com.example.infinite_track.domain.model.attendance

/**
 * Domain model untuk informasi lokasi target attendance
 */
data class TargetLocationInfo(
    val description: String, // Deskripsi dari API
    val locationName: String // Nama lokasi berdasarkan koordinat dari API
)