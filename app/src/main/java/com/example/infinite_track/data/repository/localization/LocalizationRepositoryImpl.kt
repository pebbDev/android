package com.example.infinite_track.data.repository.localization

import com.example.infinite_track.data.soucre.local.preferences.LocalizationPreference
import com.example.infinite_track.domain.repository.LocalizationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalizationRepositoryImpl @Inject constructor(
    private val localizationPreference: LocalizationPreference
) : LocalizationRepository {

    override fun getSelectedLanguage(): Flow<String> {
        return localizationPreference.getLanguage()
    }

    override suspend fun setSelectedLanguage(language: String) {
        localizationPreference.saveLanguage(language)
    }
}
