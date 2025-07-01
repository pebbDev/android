package com.example.infinite_track.domain.use_case.location

import com.example.infinite_track.domain.repository.LocationRepository
import javax.inject.Inject

/**
 * Use case untuk mendapatkan koordinat lokasi saat ini
 */
class GetCurrentCoordinatesUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    /**
     * Mendapatkan koordinat lokasi saat ini
     * @return Result berisi Pair latitude dan longitude atau error
     */
    suspend operator fun invoke(): Result<Pair<Double, Double>> {
        return locationRepository.getCurrentCoordinates()
    }
}
