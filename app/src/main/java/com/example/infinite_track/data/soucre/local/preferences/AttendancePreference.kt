package com.example.infinite_track.data.soucre.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
}
