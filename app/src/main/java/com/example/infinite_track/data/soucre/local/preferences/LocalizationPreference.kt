package com.example.infinite_track.data.soucre.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataLanguage: DataStore<Preferences> by preferencesDataStore(name = "language")

@Singleton
class LocalizationPreference @Inject constructor(private val dataLanguage: DataStore<Preferences>) {

    companion object {
        private val LANGUAGE_KEY = stringPreferencesKey("language")
    }

    fun getLanguage(): Flow<String> {
        return dataLanguage.data.map { preferences ->
            preferences[LANGUAGE_KEY] ?: "en"
        }
    }

    suspend fun saveLanguage(language: String) {
        dataLanguage.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }
}