package com.example.infinite_track.domain.use_case.language

import com.example.infinite_track.domain.repository.LocalizationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving the currently selected language
 */
class GetSelectedLanguageUseCase @Inject constructor(
    private val localizationRepository: LocalizationRepository
) {
    /**
     * Invokes the use case to get the currently selected language
     * @return Flow of String that emits the selected language code
     */
    operator fun invoke(): Flow<String> {
        return localizationRepository.getSelectedLanguage()
    }
}
