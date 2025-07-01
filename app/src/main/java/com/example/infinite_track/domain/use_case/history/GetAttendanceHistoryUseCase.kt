package com.example.infinite_track.domain.use_case.history

import com.example.infinite_track.domain.model.attendance.AttendanceHistoryPage
import com.example.infinite_track.domain.repository.AttendanceHistoryRepository
import javax.inject.Inject

/**
 * Use case for retrieving attendance history with pagination and filtering
 */
class GetAttendanceHistoryUseCase @Inject constructor(
    private val attendanceHistoryRepository: AttendanceHistoryRepository
) {
    /**
     * Invoke the use case to get attendance history
     * @param period The period to filter by (e.g., "daily", "weekly", "monthly")
     * @param page The page number to load
     * @param limit The number of items per page
     * @return Result containing the attendance history page if successful
     */
    suspend operator fun invoke(
        period: String,
        page: Int,
        limit: Int
    ): Result<AttendanceHistoryPage> {
        return attendanceHistoryRepository.getAttendanceHistory(period, page, limit)
    }
}
