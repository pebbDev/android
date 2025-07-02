package com.example.infinite_track.presentation.geofencing

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.infinite_track.data.soucre.network.request.LocationEventRequest
import com.example.infinite_track.domain.repository.AttendanceRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Worker untuk mengirim location event ke backend secara asinkron
 * Menggunakan WorkManager untuk memastikan pengiriman data yang andal
 */
@HiltWorker
class LocationEventWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val attendanceRepository: AttendanceRepository
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val KEY_EVENT_TYPE = "event_type"
        const val KEY_LOCATION_ID = "location_id"
        const val KEY_TIMESTAMP = "timestamp"
        const val TAG = "LocationEventWorker"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            // Ambil data dari inputData
            val eventType =
                inputData.getString(KEY_EVENT_TYPE) ?: return@withContext Result.failure()
            val locationId = inputData.getInt(KEY_LOCATION_ID, -1)
            val timestamp =
                inputData.getString(KEY_TIMESTAMP) ?: return@withContext Result.failure()

            // Validasi data
            if (locationId == -1) {
                return@withContext Result.failure()
            }

            // Buat request object
            val request = LocationEventRequest(
                eventType = eventType,
                locationId = locationId,
                eventTimestamp = timestamp
            )

            // Kirim ke repository
            attendanceRepository.sendLocationEvent(request).fold(
                onSuccess = {
                    android.util.Log.d(
                        TAG,
                        "Location event sent successfully: $eventType for location $locationId"
                    )
                    Result.success()
                },
                onFailure = { exception ->
                    android.util.Log.e(TAG, "Failed to send location event", exception)
                    // Retry untuk network errors, failure untuk client errors
                    when {
                        exception.message?.contains("timeout", ignoreCase = true) == true ||
                                exception.message?.contains("network", ignoreCase = true) == true ||
                                exception.message?.contains(
                                    "connection",
                                    ignoreCase = true
                                ) == true -> {
                            Result.retry()
                        }

                        else -> Result.failure()
                    }
                }
            )
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Unexpected error in LocationEventWorker", e)
            Result.failure()
        }
    }
}
