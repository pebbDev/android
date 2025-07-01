package com.example.infinite_track.data.repository.attendance

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import com.example.infinite_track.data.mapper.attendance.toActiveSession
import com.example.infinite_track.data.mapper.attendance.toDomain
import com.example.infinite_track.data.mapper.attendance.toDto
import com.example.infinite_track.data.soucre.local.preferences.AttendancePreference
import com.example.infinite_track.data.soucre.network.retrofit.ApiService
import com.example.infinite_track.domain.model.attendance.ActiveAttendanceSession
import com.example.infinite_track.domain.model.attendance.AttendanceRequestModel
import com.example.infinite_track.domain.model.attendance.TodayStatus
import com.example.infinite_track.domain.repository.AttendanceRepository
import com.example.infinite_track.utils.calculateDistance
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttendanceRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val attendancePreference: AttendancePreference
) : AttendanceRepository {
    /**
     * Checks if a user's location is within the allowed radius of a target location
     */
    private fun isWithinGeofence(
        userLocation: Location,
        targetLatitude: Double,
        targetLongitude: Double,
        radius: Int
    ): Boolean {
        val distance = calculateDistance(
            targetLatitude.toFloat(),
            targetLongitude.toFloat(),
            userLocation.latitude.toFloat(),
            userLocation.longitude.toFloat()
        )
        Log.d("Geofence", "Calculated Distance: $distance meters, Allowed radius: $radius meters")
        return distance <= radius
    }

    /**
     * Gets the current day's attendance status
     */
    override suspend fun getTodayStatus(): Result<TodayStatus> {
        return try {
            val response = apiService.getTodayStatus()
            if (response.success) {
                // Convert DTO to domain model using mapper
                Result.success(response.data.toDomain())
            } else {
                Result.failure(Exception(response.message ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Log.e("AttendanceRepository", "Error getting today's status", e)
            Result.failure(e)
        }
    }

    /**
     * Performs check-in operation
     */
    override suspend fun checkIn(
        currentUserLocation: Location,
        request: AttendanceRequestModel
    ): Result<ActiveAttendanceSession> {
        return try {
            // Get the location info from the API response
            val todayStatusResult = getTodayStatus()
            if (todayStatusResult.isFailure) {
                return Result.failure(todayStatusResult.exceptionOrNull()
                    ?: Exception("Failed to get location information"))
            }

            val todayStatus = todayStatusResult.getOrNull()
            val activeLocation = todayStatus?.activeLocation
                ?: return Result.failure(Exception("No active location found"))

            // Validate location
            val isWithinGeofence = isWithinGeofence(
                currentUserLocation,
                activeLocation.latitude,
                activeLocation.longitude,
                activeLocation.radius
            )

            if (!isWithinGeofence) {
                return Result.failure(Exception("Anda berada di luar radius lokasi yang diizinkan."))
            }

            // Convert domain model to DTO using mapper
            val requestDto = request.toDto()

            // Call API for check-in
            val response = apiService.checkIn(requestDto)

            if (response.success) {
                // Save the attendance ID for later checkout
                attendancePreference.saveActiveAttendanceId(response.data.idAttendance)
                // Convert DTO to ActiveAttendanceSession domain model using mapper
                Result.success(response.data.toActiveSession())
            } else {
                Result.failure(Exception(response.message ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Log.e("AttendanceRepository", "Error during check-in", e)
            Result.failure(e)
        }
    }

    /**
     * Performs check-out operation
     */
    override suspend fun checkOut(): Result<ActiveAttendanceSession> {
        return try {
            // Get the active attendance ID
            val attendanceId = getActiveAttendanceId()
                ?: return Result.failure(Exception("No active attendance session found"))

            // Call API for check-out
            val response = apiService.checkOut(attendanceId)

            if (response.success) {
                // Clear the active attendance ID
                attendancePreference.clearActiveAttendanceId()
                // Convert DTO to ActiveAttendanceSession domain model using mapper
                Result.success(response.data.toActiveSession())
            } else {
                Result.failure(Exception(response.message ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Log.e("AttendanceRepository", "Error during check-out", e)
            Result.failure(e)
        }
    }

    /**
     * Retrieves the active attendance ID from preferences
     */
    override suspend fun getActiveAttendanceId(): Int? {
        return attendancePreference.getActiveAttendanceId().first()
    }

    /**
     * Gets the attendance history for a specific month
     */
}