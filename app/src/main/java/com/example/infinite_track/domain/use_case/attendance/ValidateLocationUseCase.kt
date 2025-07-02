package com.example.infinite_track.domain.use_case.attendance

import com.example.infinite_track.utils.calculateDistance
import javax.inject.Inject

/**
 * Use case for validating if user location is within target location radius
 */
class ValidateLocationUseCase @Inject constructor() {

    /**
     * Validates if user coordinates are within the target location radius
     * @param userLatitude User's current latitude
     * @param userLongitude User's current longitude
     * @param targetLatitude Target location latitude
     * @param targetLongitude Target location longitude
     * @param radius Allowed radius in meters
     * @return true if user is within radius, false otherwise
     */
    operator fun invoke(
        userLatitude: Double,
        userLongitude: Double,
        targetLatitude: Double,
        targetLongitude: Double,
        radius: Int
    ): Boolean {
        val distance = calculateDistance(
            targetLatitude.toFloat(),
            targetLongitude.toFloat(),
            userLatitude.toFloat(),
            userLongitude.toFloat()
        )
        return distance <= radius
    }
}
