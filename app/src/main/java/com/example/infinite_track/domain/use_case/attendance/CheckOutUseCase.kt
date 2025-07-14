package com.example.infinite_track.domain.use_case.attendance

import com.example.infinite_track.domain.model.attendance.ActiveAttendanceSession
import com.example.infinite_track.domain.repository.AttendanceRepository
import com.example.infinite_track.domain.use_case.location.GetCurrentCoordinatesUseCase
import com.example.infinite_track.presentation.geofencing.GeofenceManager
import javax.inject.Inject

/**
 * Use case for check-out operation
 * Orchestrates the check-out process by getting attendance info, coordinates, and managing geofence
 */
class CheckOutUseCase @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val getCurrentCoordinatesUseCase: GetCurrentCoordinatesUseCase,
    private val geofenceManager: GeofenceManager
) {
    /**
     * Performs check-out operation
     * No parameters needed - gets all required data from repository and current location
     * @return Result containing ActiveAttendanceSession on success or exception on failure
     */
    suspend operator fun invoke(): Result<ActiveAttendanceSession> {
        return try {
            // 1. Get active attendance ID
            val attendanceId = attendanceRepository.getActiveAttendanceId()
                ?: return Result.failure(Exception("No active attendance session found"))

            // 2. Get current location ID from today's status (needed for geofence removal)
            val todayStatusResult = attendanceRepository.getTodayStatus()
            if (todayStatusResult.isFailure) {
                return Result.failure(
                    todayStatusResult.exceptionOrNull()
                        ?: Exception("Failed to get today's status")
                )
            }

            val todayStatus = todayStatusResult.getOrNull()!!
            val locationId = todayStatus.activeLocation?.locationId
                ?: return Result.failure(Exception("No location information found"))

            // 3. Get current real-time GPS coordinates
            val coordinatesResult = getCurrentCoordinatesUseCase()
            if (coordinatesResult.isFailure) {
                return Result.failure(
                    coordinatesResult.exceptionOrNull()
                        ?: Exception("Failed to get current location")
                )
            }

            val currentCoordinates = coordinatesResult.getOrNull()!!

            // 4. Call repository to perform check-out
            // Backend will handle location validation
            val checkOutResult = attendanceRepository.checkOut(
                attendanceId = attendanceId,
                latitude = currentCoordinates.first,
                longitude = currentCoordinates.second
            )

            // 5. If check-out successful, remove geofence
            if (checkOutResult.isSuccess) {
                try {
                    geofenceManager.removeGeofence(locationId.toString())
                    android.util.Log.d(
                        "CheckOutUseCase",
                        "Geofence removed for location $locationId"
                    )
                } catch (e: Exception) {
                    android.util.Log.e("CheckOutUseCase", "Failed to remove geofence", e)
                    // Don't fail the entire check-out process if geofence removal fails
                }
            }

            checkOutResult

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
