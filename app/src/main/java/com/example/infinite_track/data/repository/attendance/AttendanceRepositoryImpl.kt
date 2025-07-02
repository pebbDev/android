package com.example.infinite_track.data.repository.attendance

import android.util.Log
import com.example.infinite_track.data.mapper.attendance.toActiveSession
import com.example.infinite_track.data.mapper.attendance.toDomain
import com.example.infinite_track.data.mapper.attendance.toDto
import com.example.infinite_track.data.soucre.local.preferences.AttendancePreference
import com.example.infinite_track.data.soucre.network.request.LocationEventRequest
import com.example.infinite_track.data.soucre.network.retrofit.ApiService
import com.example.infinite_track.domain.model.attendance.ActiveAttendanceSession
import com.example.infinite_track.domain.model.attendance.AttendanceRequestModel
import com.example.infinite_track.domain.model.attendance.Location
import com.example.infinite_track.domain.model.attendance.TodayStatus
import com.example.infinite_track.domain.repository.AttendanceRepository
import com.example.infinite_track.presentation.geofencing.GeofenceManager
import com.example.infinite_track.utils.calculateDistance
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttendanceRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val attendancePreference: AttendancePreference,
    private val geofenceManager: GeofenceManager
) : AttendanceRepository {

    companion object {
        private const val TAG = "AttendanceRepository"
    }

    /**
     * Checks if a user's location is within the allowed radius of a target location
     */
    private fun isWithinGeofence(
        userLocation: android.location.Location,
        targetLocation: Location
    ): Boolean {
        val distance = calculateDistance(
            targetLocation.latitude.toFloat(),
            targetLocation.longitude.toFloat(),
            userLocation.latitude.toFloat(),
            userLocation.longitude.toFloat()
        )
        Log.d(
            TAG,
            "Calculated Distance: $distance meters, Allowed radius: ${targetLocation.radius} meters"
        )
        return distance <= targetLocation.radius
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
            Log.e(TAG, "Error getting today's status", e)
            Result.failure(e)
        }
    }

    /**
     * Performs check-in operation with simplified logic
     * No longer calls getTodayStatus() - relies on provided targetLocation parameter
     */
    override suspend fun checkIn(
        currentUserLocation: android.location.Location,
        targetLocation: Location,
        request: AttendanceRequestModel
    ): Result<ActiveAttendanceSession> {
        return try {
            // Validate location using provided targetLocation parameter
            val isWithinGeofence = isWithinGeofence(currentUserLocation, targetLocation)

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

                // Start geofence monitoring after successful check-in using provided targetLocation
                try {
                    geofenceManager.addGeofence(
                        id = targetLocation.locationId.toString(),
                        latitude = targetLocation.latitude,
                        longitude = targetLocation.longitude,
                        radius = targetLocation.radius.toFloat()
                    )
                    Log.d(
                        TAG,
                        "Geofence monitoring started for location ${targetLocation.locationId}"
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to start geofence monitoring", e)
                    // Don't fail the entire check-in process if geofence setup fails
                }

                // Convert DTO to ActiveAttendanceSession domain model using mapper
                Result.success(response.data.toActiveSession())
            } else {
                Result.failure(Exception(response.message ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error during check-in", e)
            Result.failure(e)
        }
    }

    /**
     * Performs check-out operation with simplified logic
     * No longer calls getTodayStatus() - relies on provided locationId parameter
     */
    override suspend fun checkOut(locationId: Int): Result<ActiveAttendanceSession> {
        return try {
            // Get the active attendance ID
            val attendanceId = getActiveAttendanceId()
                ?: return Result.failure(Exception("No active attendance session found"))

            // Call API for check-out
            val response = apiService.checkOut(attendanceId)

            if (response.success) {
                // Clear the active attendance ID
                attendancePreference.clearActiveAttendanceId()

                // Stop geofence monitoring after successful check-out using provided locationId
                try {
                    geofenceManager.removeGeofence(locationId.toString())
                    Log.d(TAG, "Geofence monitoring stopped for location $locationId")
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to stop geofence monitoring", e)
                    // Don't fail the entire check-out process if geofence removal fails
                }

                // Convert DTO to ActiveAttendanceSession domain model using mapper
                Result.success(response.data.toActiveSession())
            } else {
                Result.failure(Exception(response.message ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error during check-out", e)
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
     * Sends location event (ENTER/EXIT) to backend
     */
    override suspend fun sendLocationEvent(request: LocationEventRequest): Result<Unit> {
        return try {
            val response = apiService.sendLocationEvent(request)

            if (response.isSuccessful) {
                Log.d(
                    TAG,
                    "Location event sent successfully: ${request.eventType} for location ${request.locationId}"
                )
                Result.success(Unit)
            } else {
                val errorMsg =
                    "Failed to send location event: ${response.code()} ${response.message()}"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending location event", e)
            Result.failure(e)
        }
    }
}