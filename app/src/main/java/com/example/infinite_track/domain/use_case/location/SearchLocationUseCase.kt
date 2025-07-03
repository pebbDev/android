package com.example.infinite_track.domain.use_case.location

import com.example.infinite_track.domain.model.location.LocationResult
import com.example.infinite_track.domain.repository.LocationRepository
import javax.inject.Inject

/**
 * Use Case untuk pencarian lokasi
 * Memisahkan logika bisnis pencarian dari ViewModel
 */
class SearchLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {

    /**
     * Melakukan pencarian lokasi berdasarkan query
     *
     * @param query Query pencarian
     * @param userLatitude Latitude pengguna untuk proximity search (opsional)
     * @param userLongitude Longitude pengguna untuk proximity search (opsional)
     * @return Result berisi list LocationResult
     */
    suspend operator fun invoke(
        query: String,
        userLatitude: Double? = null,
        userLongitude: Double? = null
    ): Result<List<LocationResult>> {
        // Validasi input
        if (query.isBlank()) {
            return Result.success(emptyList())
        }

        // Panggil repository
        return locationRepository.searchLocation(
            query = query.trim(),
            userLatitude = userLatitude,
            userLongitude = userLongitude
        )
    }
}
