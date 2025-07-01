package com.example.infinite_track.domain.repository

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
}
