package com.example.infinite_track.data.soucre.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

private val Context.attendanceDataStore: DataStore<Preferences> by preferencesDataStore(name = "attendance_session")

@Singleton
class AttendancePreference @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.attendanceDataStore

    companion object {
        private val ACTIVE_ATTENDANCE_ID_KEY = intPreferencesKey("active_attendance_id")
        private val IS_INSIDE_GEOFENCE_KEY = booleanPreferencesKey("is_inside_geofence")
        private val LAST_GEOFENCE_REQUEST_ID_KEY = stringPreferencesKey("last_geofence_request_id")
        private val LAST_GEOFENCE_LAT_KEY = stringPreferencesKey("last_geofence_lat")
        private val LAST_GEOFENCE_LNG_KEY = stringPreferencesKey("last_geofence_lng")
        private val LAST_GEOFENCE_RADIUS_KEY = intPreferencesKey("last_geofence_radius")
    }

    /**
     * Save the active attendance ID to DataStore
     */
    suspend fun saveActiveAttendanceId(id: Int) {
        dataStore.edit { preferences ->
            preferences[ACTIVE_ATTENDANCE_ID_KEY] = id
        }
    }

    /**
     * Get the active attendance ID as a Flow
     */
    fun getActiveAttendanceId(): Flow<Int?> {
        return dataStore.data.map { preferences ->
            preferences[ACTIVE_ATTENDANCE_ID_KEY]
        }
    }

    /**
     * Clear the active attendance ID when checkout is successful
     */
    suspend fun clearActiveAttendanceId() {
        dataStore.edit { preferences ->
            preferences.remove(ACTIVE_ATTENDANCE_ID_KEY)
        }
    }

    /**
     * Persist the last registered geofence request ID for later removal
     */
    suspend fun saveLastGeofenceRequestId(requestId: String) {
        dataStore.edit { preferences ->
            preferences[LAST_GEOFENCE_REQUEST_ID_KEY] = requestId
        }
    }

    /**
     * Retrieve the last registered geofence request ID
     */
    fun getLastGeofenceRequestId(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[LAST_GEOFENCE_REQUEST_ID_KEY]
        }
    }

    /**
     * Clear the stored last geofence request ID
     */
    suspend fun clearLastGeofenceRequestId() {
        dataStore.edit { preferences ->
            preferences.remove(LAST_GEOFENCE_REQUEST_ID_KEY)
        }
    }

    /**
     * Get the user's geofence status as a Flow
     */
    fun isUserInsideGeofence(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[IS_INSIDE_GEOFENCE_KEY] ?: false
        }
    }

    /**
     * Save the user's geofence status
     */
    suspend fun setUserInsideGeofence(isInside: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_INSIDE_GEOFENCE_KEY] = isInside
        }
    }

    /**
     * Persist last geofence parameters for re-registration after reboot
     */
    suspend fun saveLastGeofenceParameters(
        requestId: String,
        latitude: Double,
        longitude: Double,
        radius: Int
    ) {
        dataStore.edit { preferences ->
            preferences[LAST_GEOFENCE_REQUEST_ID_KEY] = requestId
            preferences[LAST_GEOFENCE_LAT_KEY] = latitude.toString()
            preferences[LAST_GEOFENCE_LNG_KEY] = longitude.toString()
            preferences[LAST_GEOFENCE_RADIUS_KEY] = radius
        }
    }

    /**
     * Retrieve last geofence parameters if available
     */
    suspend fun getLastGeofenceParameters(): Triple<String, Pair<Double, Double>, Int>? {
        val prefs = dataStore.data.firstOrNull()
        return try {
            val requestId = prefs?.get(LAST_GEOFENCE_REQUEST_ID_KEY) ?: return null
            val lat = prefs.get(LAST_GEOFENCE_LAT_KEY)?.toDouble() ?: return null
            val lng = prefs.get(LAST_GEOFENCE_LNG_KEY)?.toDouble() ?: return null
            val radius = prefs.get(LAST_GEOFENCE_RADIUS_KEY) ?: return null
            Triple(requestId, Pair(lat, lng), radius)
        } catch (_: Exception) {
            null
        }
    }
}
