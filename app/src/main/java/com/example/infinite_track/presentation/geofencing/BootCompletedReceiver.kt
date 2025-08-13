package com.example.infinite_track.presentation.geofencing

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.firstOrNull

class BootCompletedReceiver : BroadcastReceiver() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface BootReceiverEntryPoint {
        fun geofenceManager(): GeofenceManager
        fun attendancePreference(): com.example.infinite_track.data.soucre.local.preferences.AttendancePreference
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            BootReceiverEntryPoint::class.java
        )
        val geofenceManager = entryPoint.geofenceManager()
        val attendancePreference = entryPoint.attendancePreference()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val activeId = attendancePreference.getActiveAttendanceId().firstOrNull()
                if (activeId == null) {
                    Log.d("BootCompletedReceiver", "No active session. Skip geofence re-registration.")
                    return@launch
                }
                val params = attendancePreference.getLastGeofenceParameters()
                if (params == null) {
                    Log.d("BootCompletedReceiver", "No stored geofence params. Nothing to restore.")
                    return@launch
                }
                val (requestId, latLng, radius) = params
                val (lat, lng) = latLng
                Log.d("BootCompletedReceiver", "Re-registering geofence after boot: $requestId")
                geofenceManager.addGeofence(requestId, lat, lng, radius.toFloat())
            } catch (e: Exception) {
                Log.e("BootCompletedReceiver", "Failed to re-register geofence after boot", e)
            }
        }
    }
}