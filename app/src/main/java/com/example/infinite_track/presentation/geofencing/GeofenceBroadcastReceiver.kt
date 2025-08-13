package com.example.infinite_track.presentation.geofencing

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.infinite_track.data.soucre.local.preferences.AttendancePreference
import com.example.infinite_track.data.worker.LocationEventWorker
import com.example.infinite_track.utils.NotificationHelper
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

/**
 * BroadcastReceiver untuk menangani events geofence secara cerdas
 * Hanya memproses event jika ada sesi kerja yang aktif
 */
class GeofenceBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "GeofenceReceiver"
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface GeofenceReceiverEntryPoint {
        fun attendancePreference(): AttendancePreference
    }

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent?.hasError() == true) {
            Log.e(TAG, "Geofence Error code: ${geofencingEvent.errorCode}")
            return
        }

        val geofenceTransition = geofencingEvent?.geofenceTransition
        val triggeringGeofences = geofencingEvent?.triggeringGeofences ?: return

        // Convert transition type to string
        val eventType = when (geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> "ENTER"
            Geofence.GEOFENCE_TRANSITION_EXIT -> "EXIT"
            Geofence.GEOFENCE_TRANSITION_DWELL -> "DWELL"
            else -> {
                Log.w(TAG, "Unknown geofence transition: $geofenceTransition")
                return
            }
        }

        // Get dependencies using Hilt EntryPoint
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            GeofenceReceiverEntryPoint::class.java
        )
        val attendancePreference = entryPoint.attendancePreference()

        // Use coroutine to check active session
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Check if there's an active attendance session
                val activeAttendanceId = attendancePreference.getActiveAttendanceId().first()

                if (activeAttendanceId == null) {
                    Log.d(TAG, "No active attendance session found. Ignoring geofence event.")
                    return@launch
                }

                Log.d(
                    TAG,
                    "Active session found (ID: $activeAttendanceId). Processing geofence event: $eventType"
                )

                // Update geofence status in preferences based on event type
                when (eventType) {
                    "ENTER", "DWELL" -> attendancePreference.setUserInsideGeofence(true)
                    "EXIT" -> attendancePreference.setUserInsideGeofence(false)
                }

                // Process each triggered geofence
                triggeringGeofences.forEach { geofence ->
                    processGeofenceEvent(context, geofence, eventType)
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error processing geofence event", e)
            }
        }
    }

    private fun processGeofenceEvent(context: Context, geofence: Geofence, eventType: String) {
        try {
            // Use requestId string directly to support non-numeric IDs
            val requestId = geofence.requestId

            // Generate timestamp in ISO 8601 UTC format
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val timestamp = formatter.format(Date())

            // Show immediate notification to user
            val locationName = requestId // Use requestId as location name
            NotificationHelper.showGeofenceNotification(context, eventType, locationName)

            // Prepare data for WorkManager
            val workData = Data.Builder()
                .putString(LocationEventWorker.KEY_EVENT_TYPE, eventType)
                .putString(LocationEventWorker.KEY_LOCATION_ID, requestId)
                .putString(LocationEventWorker.KEY_EVENT_TIMESTAMP, timestamp)
                .build()

            // Create constraints - require network connection
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            // Create work request with backoff policy
            val workRequest = OneTimeWorkRequestBuilder<LocationEventWorker>()
                .setInputData(workData)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
                .addTag("location_event_$requestId")
                .build()

            // Enqueue unique work per requestId to prevent duplicates
            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    "location_event_unique_$requestId",
                    ExistingWorkPolicy.APPEND,
                    workRequest
                )

            Log.d(
                TAG,
                "Location event work enqueued: $eventType for requestId $requestId at $timestamp"
            )

        } catch (e: Exception) {
            Log.e(TAG, "Error processing geofence event for ${geofence.requestId}", e)
        }
    }
}