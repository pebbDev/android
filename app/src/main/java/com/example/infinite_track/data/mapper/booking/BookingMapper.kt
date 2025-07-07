package com.example.infinite_track.data.mapper.booking

import com.example.infinite_track.data.soucre.network.response.booking.BookingItem
import com.example.infinite_track.domain.model.booking.BookingHistoryItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun BookingItem.toDomain(): BookingHistoryItem {
    return BookingHistoryItem(
        id = this.id ?: "", // Handle null id
        locationDescription = this.location?.description ?: "Unknown Location", // Handle null location
        scheduleDate = formatScheduleDate(this.scheduleDate ?: ""),
        status = formatStatus(this.status ?: "Unknown"),
        notes = this.notes ?: "No notes provided",
        suitabilityLabel = formatSuitabilityLabel(this.suitabilityScore ?: 0f)
    )
}

private fun formatScheduleDate(dateString: String): String {
    return try {
        val date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        date.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH))
    } catch (e: Exception) {
        dateString // Return original if parsing fails
    }
}

private fun formatStatus(status: String): String {
    return status.lowercase().replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
}

private fun formatSuitabilityLabel(score: Float): String {
    return when {
        score >= 80f -> "Excellent"
        score >= 60f -> "Good"
        score >= 40f -> "Fair"
        else -> "Poor"
    }
}
