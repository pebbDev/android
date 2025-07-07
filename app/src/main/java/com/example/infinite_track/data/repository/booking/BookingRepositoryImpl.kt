package com.example.infinite_track.data.repository.booking

import com.example.infinite_track.data.mapper.booking.toDomain
import com.example.infinite_track.data.soucre.network.retrofit.ApiService
import com.example.infinite_track.domain.model.booking.BookingHistoryItem
import com.example.infinite_track.domain.repository.BookingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : BookingRepository {

    override suspend fun getBookingHistory(
        status: String?,
        page: Int,
        limit: Int,
        sortBy: String,
        sortOrder: String
    ): Result<List<BookingHistoryItem>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getBookingHistory(
                    status = status,
                    page = page,
                    limit = limit,
                    sortBy = sortBy,
                    sortOrder = sortOrder
                )
                val bookingHistoryItems = response.data.bookings.map { it.toDomain() }
                Result.success(bookingHistoryItems)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
