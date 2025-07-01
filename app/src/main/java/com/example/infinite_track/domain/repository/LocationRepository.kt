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
}
