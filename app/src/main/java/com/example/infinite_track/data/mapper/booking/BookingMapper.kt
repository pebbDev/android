package com.example.infinite_track.data.mapper.booking

import com.example.infinite_track.data.soucre.network.response.booking.BookingItem
import com.example.infinite_track.domain.model.booking.BookingHistoryItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun BookingItem.toDomain(): BookingHistoryItem {
    return BookingHistoryItem(
        id = this.bookingId.toString(),
        locationDescription = this.location.description,
        scheduleDate = formatScheduleDate(this.scheduleDate),
        status = formatStatus(this.status),
        notes = this.notes ?: "",
        suitabilityLabel = this.suitabilityLabel ?: formatSuitabilityLabel(this.suitabilityScore ?: 0f)
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
