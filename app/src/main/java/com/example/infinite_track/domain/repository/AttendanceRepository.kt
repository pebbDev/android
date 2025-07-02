package com.example.infinite_track.domain.repository

import com.example.infinite_track.data.soucre.network.request.LocationEventRequest
import com.example.infinite_track.domain.model.attendance.ActiveAttendanceSession
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
     * @param currentUserLocation The current location of the user (Android Location for GPS data)
     * @param targetLocation The target location for attendance (Domain model)
     * @param request The attendance request containing necessary data for check-in
     */
    suspend fun checkIn(
        currentUserLocation: android.location.Location,
        targetLocation: com.example.infinite_track.domain.model.attendance.Location,
        request: AttendanceRequestModel
    ): Result<ActiveAttendanceSession>

    /**
     * Performs check-out operation for the active attendance session
     * @param locationId The ID of the location to remove geofence for
     */
    suspend fun checkOut(locationId: Int): Result<ActiveAttendanceSession>

    /**
     * Retrieves the active attendance ID from preferences
     */
    suspend fun getActiveAttendanceId(): Int?

    /**
     * Sends location event (ENTER/EXIT) to backend
     * @param request The location event request containing event type, location ID, and timestamp
     */
    suspend fun sendLocationEvent(request: LocationEventRequest): Result<Unit>
}
