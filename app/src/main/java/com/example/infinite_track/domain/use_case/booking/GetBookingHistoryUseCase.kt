package com.example.infinite_track.domain.use_case.booking

import com.example.infinite_track.domain.model.booking.BookingHistoryItem
import com.example.infinite_track.domain.repository.BookingRepository
import javax.inject.Inject

class GetBookingHistoryUseCase @Inject constructor(
    private val bookingRepository: BookingRepository
) {
    suspend operator fun invoke(limit: Int): Result<List<BookingHistoryItem>> {
        return bookingRepository.getBookingHistory(limit)
    }
}
