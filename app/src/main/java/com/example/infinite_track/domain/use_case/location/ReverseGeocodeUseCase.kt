package com.example.infinite_track.domain.use_case.location

import com.example.infinite_track.domain.model.location.LocationResult
import com.example.infinite_track.domain.repository.LocationRepository
import javax.inject.Inject

/**
 * Use case for reverse geocoding - converting coordinates to address
 */
class ReverseGeocodeUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    /**
     * Execute reverse geocoding for given coordinates
     * @param latitude Latitude coordinate
     * @param longitude Longitude coordinate
     * @return Result containing LocationResult with address information or error
     */
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double
    ): Result<LocationResult> {
        return locationRepository.reverseGeocode(latitude, longitude)
    }
}
