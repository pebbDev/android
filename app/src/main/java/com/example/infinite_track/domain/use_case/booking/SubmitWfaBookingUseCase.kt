package com.example.infinite_track.domain.use_case.booking

import com.example.infinite_track.domain.repository.BookingRepository
import javax.inject.Inject

class SubmitWfaBookingUseCase @Inject constructor(
    private val bookingRepository: BookingRepository
) {
    suspend operator fun invoke(
        scheduleDate: String,
        latitude: Double,
        longitude: Double,
        radius: Int,
        description: String,
        notes: String
    ): Result<Unit> {
        return bookingRepository.submitBooking(
            scheduleDate = scheduleDate,
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            description = description,
            notes = notes
        )
    }
}
