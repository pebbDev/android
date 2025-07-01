package com.example.infinite_track.data.soucre.repository.language

import com.example.infinite_track.data.soucre.local.preferences.LocalizationPreference
import com.example.infinite_track.domain.repository.LocalizationRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class LocalizationRepositoryImpl @Inject constructor(
    private val preferenceManager: LocalizationPreference
) : LocalizationRepository {
    override fun getSelectedLanguage(): Flow<String> {
        return preferenceManager.getLanguage()
    }

    override suspend fun setSelectedLanguage(language: String) {
        preferenceManager.saveLanguage(language)
    }
}
