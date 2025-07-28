package com.example.infinite_track.presentation.geofencing

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeofenceManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val geofencingClient = LocationServices.getGeofencingClient(context)
    private val TAG = "GeofenceManager"

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    /**
     * Remove all geofences registered by this application
     * This ensures we never hit the system limit of ~100 geofences
     */
    fun removeAllGeofences() {
        geofencingClient.removeGeofences(geofencePendingIntent).run {
            addOnSuccessListener {
                Log.d(TAG, "Semua geofence berhasil dihapus")
            }
            addOnFailureListener { exception ->
                Log.e(TAG, "Gagal menghapus semua geofence", exception)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun addGeofence(id: String, latitude: Double, longitude: Double, radius: Float) {
        // STEP 1: Always remove all existing geofences first (clean slate approach)
        Log.d(TAG, "Membersihkan semua geofence sebelum menambah yang baru...")

        geofencingClient.removeGeofences(geofencePendingIntent).run {
            addOnSuccessListener {
                Log.d(TAG, "Semua geofence berhasil dihapus, sekarang menambah geofence baru...")

                // STEP 2: Create and add the new geofence ONLY after removal is successful
                val geofence = Geofence.Builder()
                    .setRequestId(id)
                    .setCircularRegion(latitude, longitude, radius)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build()

                val geofencingRequest = GeofencingRequest.Builder()
                    .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                    .addGeofence(geofence)
                    .build()

                geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
                    addOnSuccessListener {
                        Log.d(
                            TAG,
                            "Geofence berhasil ditambahkan: $id (lat: $latitude, lng: $longitude, radius: ${radius}m)"
                        )
                    }
                    addOnFailureListener { exception ->
                        Log.e(TAG, "Gagal menambahkan geofence: $id", exception)
                    }
                }
            }
            addOnFailureListener { exception ->
                Log.e(
                    TAG,
                    "Gagal menghapus semua geofence, geofence baru tidak akan ditambahkan",
                    exception
                )
            }
        }
    }

    fun removeGeofence(id: String) {
        geofencingClient.removeGeofences(listOf(id)).run {
            addOnSuccessListener { Log.d(TAG, "Geofence berhasil dihapus: $id") }
            addOnFailureListener { Log.e(TAG, "Gagal menghapus geofence: $id", it) }
        }
    }
}
