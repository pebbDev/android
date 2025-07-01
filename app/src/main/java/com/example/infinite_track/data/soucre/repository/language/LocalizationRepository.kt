package com.example.infinite_track.data.soucre.repository.language

import com.example.infinite_track.data.soucre.local.preferences.LocalizationPreference
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow


@Singleton
class LocalizationRepository @Inject constructor(
    private val preferenceManager: LocalizationPreference
) {
    fun getSelectedLanguage(): Flow<String> {
        return preferenceManager.getLanguage()
    }

    suspend fun setSelectedLanguage(language: String) {
        preferenceManager.saveLanguage(language)
    }
}