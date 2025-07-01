package com.example.infinite_track.domain.repository

import android.location.Location
import com.example.infinite_track.domain.model.attendance.ActiveAttendanceSession
import com.example.infinite_track.domain.model.attendance.AttendanceRecord
import com.example.infinite_track.domain.model.attendance.AttendanceRequestModel
import com.example.infinite_track.domain.model.attendance.TodayStatus

/**
 * Repository interface for Attendance operations following Clean Architecture principles.
 * This interface defines the contract for attendance-related operations
 * to be implemented by concrete repository classes.
 */
interface AttendanceRepository {
    /**
     * Gets the current day's attendance status
     */
    suspend fun getTodayStatus(): Result<TodayStatus>

    /**
     * Performs check-in operation
     * @param currentUserLocation The current location of the user
     * @param request The attendance request containing necessary data for check-in
     */
    suspend fun checkIn(
        currentUserLocation: Location,
        request: AttendanceRequestModel
    ): Result<ActiveAttendanceSession>

    /**
     * Performs check-out operation for the active attendance session
     */
    suspend fun checkOut(): Result<ActiveAttendanceSession>

    /**
     * Retrieves the active attendance ID from preferences
     */
    suspend fun getActiveAttendanceId(): Int?
}
