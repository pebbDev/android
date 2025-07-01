package com.example.infinite_track.domain.repository

import com.example.infinite_track.domain.model.attendance.AttendanceHistoryPage

/**
 * Repository interface for attendance history operations
 */
interface AttendanceHistoryRepository {
    /**
     * Get attendance history with pagination and filtering
     * @param period The period to filter by (e.g., "daily", "weekly", "monthly")
     * @param page The page number to load
     * @param limit The number of items per page
     * @return Result containing the attendance history page if successful
     */
    suspend fun getAttendanceHistory(
        period: String,
        page: Int,
        limit: Int
    ): Result<AttendanceHistoryPage>
}
