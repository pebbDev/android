package com.example.infinite_track.domain.repository

import com.example.infinite_track.domain.model.booking.BookingHistoryItem

interface BookingRepository {
    suspend fun getBookingHistory(limit: Int): Result<List<BookingHistoryItem>>
}
