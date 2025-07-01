package com.example.infinite_track.presentation.geofencing

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.example.infinite_track.utils.NotificationHelper

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private val TAG = "GeofenceReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent?.hasError() == true) {
            Log.e(TAG, "Geofence Error code: " + geofencingEvent.errorCode)
            return
        }

        val geofenceTransition = geofencingEvent?.geofenceTransition
        val triggeringGeofences = geofencingEvent?.triggeringGeofences ?: return

        val transitionType = when (geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> "Anda telah memasuki area"
            Geofence.GEOFENCE_TRANSITION_EXIT -> "Anda telah meninggalkan area"
            else -> null
        }

        transitionType?.let {
            val geofenceId = triggeringGeofences.first().requestId
            val message = "$it $geofenceId."
            Log.d(TAG, message)
            NotificationHelper.showGeofenceNotification(context, message)
        }
    }
}