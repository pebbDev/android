package com.example.infinite_track.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for localization operations
 * This is in the domain layer and does not depend on any implementation details
 */
interface LocalizationRepository {
    /**
     * Get the currently selected language as a Flow
     * @return Flow of String that emits the selected language code
     */
    fun getSelectedLanguage(): Flow<String>

    /**
     * Set the selected language
     * @param language The language code to set
     */
    suspend fun setSelectedLanguage(language: String)
}
