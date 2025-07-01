package com.example.infinite_track.domain.use_case.language

import com.example.infinite_track.domain.repository.LocalizationRepository
import javax.inject.Inject

/**
 * Use case for setting the selected language
 */
class SetSelectedLanguageUseCase @Inject constructor(
    private val localizationRepository: LocalizationRepository
) {
    /**
     * Invokes the use case to set the selected language
     * @param language The language code to set
     */
    suspend operator fun invoke(language: String) {
        localizationRepository.setSelectedLanguage(language)
    }
}
