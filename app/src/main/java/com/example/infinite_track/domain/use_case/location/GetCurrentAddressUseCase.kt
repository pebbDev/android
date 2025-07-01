package com.example.infinite_track.domain.use_case.location

import com.example.infinite_track.domain.repository.LocationRepository
import javax.inject.Inject

/**
 * Use case for getting the current address based on device location
 */
class GetCurrentAddressUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    /**
     * Invokes the use case to get the current address
     * @return Result containing the formatted address string or an error
     */
    suspend operator fun invoke(): Result<String> {
        return locationRepository.getCurrentAddress()
    }
}
