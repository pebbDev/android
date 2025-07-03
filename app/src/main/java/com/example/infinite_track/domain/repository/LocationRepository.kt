package com.example.infinite_track.domain.repository

import com.example.infinite_track.domain.model.location.LocationResult

/**
 * Repository interface for location-related operations
 */
interface LocationRepository {
    /**
     * Gets the current address based on device location
     * @return Result containing the formatted address string or an error
     */
    suspend fun getCurrentAddress(): Result<String>

    /**
     * Gets the current location coordinates
     * @return Result containing Pair of latitude and longitude or an error
     */
    suspend fun getCurrentCoordinates(): Result<Pair<Double, Double>>

    /**
     * Search for locations based on query text
     * @param query Search query
     * @param userLatitude User's current latitude for proximity search (optional)
     * @param userLongitude User's current longitude for proximity search (optional)
     * @return Result containing list of LocationResult or error
     */
    suspend fun searchLocation(
        query: String,
        userLatitude: Double? = null,
        userLongitude: Double? = null
    ): Result<List<LocationResult>>
}
