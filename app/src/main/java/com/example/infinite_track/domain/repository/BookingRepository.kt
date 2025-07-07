package com.example.infinite_track.domain.repository

import com.example.infinite_track.domain.model.booking.BookingHistoryItem

interface BookingRepository {
    suspend fun getBookingHistory(
        status: String? = null,
        page: Int = 1,
        limit: Int = 10,
        sortBy: String = "created_at",
        sortOrder: String = "DESC"
    ): Result<List<BookingHistoryItem>>
}
