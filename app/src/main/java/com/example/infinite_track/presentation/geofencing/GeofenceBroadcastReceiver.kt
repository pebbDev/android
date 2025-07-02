package com.example.infinite_track.presentation.geofencing

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.infinite_track.data.soucre.local.preferences.AttendancePreference
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
                    "ENTER" -> attendancePreference.setUserInsideGeofence(true)
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
            // Extract location ID from geofence request ID
            val locationId = geofence.requestId.toIntOrNull() ?: run {
                Log.e(TAG, "Invalid geofence request ID: ${geofence.requestId}")
                return
            }

            // Generate timestamp in ISO format
            val timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                .format(Date())

            // Show immediate notification to user
            val message = when (eventType) {
                "ENTER" -> "Anda telah memasuki area kerja"
                "EXIT" -> "Anda telah meninggalkan area kerja"
                else -> "Event lokasi: $eventType"
            }
            NotificationHelper.showGeofenceNotification(context, message)

            // Prepare data for WorkManager
            val workData = Data.Builder()
                .putString(LocationEventWorker.KEY_EVENT_TYPE, eventType)
                .putInt(LocationEventWorker.KEY_LOCATION_ID, locationId)
                .putString(LocationEventWorker.KEY_TIMESTAMP, timestamp)
                .build()

            // Create constraints - require network connection
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            // Create work request
            val workRequest = OneTimeWorkRequestBuilder<LocationEventWorker>()
                .setInputData(workData)
                .setConstraints(constraints)
                .addTag("location_event_$locationId")
                .build()

            // Enqueue work
            WorkManager.getInstance(context).enqueue(workRequest)

            Log.d(
                TAG,
                "Location event work enqueued: $eventType for location $locationId at $timestamp"
            )

        } catch (e: Exception) {
            Log.e(TAG, "Error processing geofence event for ${geofence.requestId}", e)
        }
    }
}