package com.example.infinite_track.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.example.infinite_track.domain.repository.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * Implementation of the LocationRepository interface
 */
class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationRepository {

    /**
     * Gets the current address based on device location
     * @return Result containing the formatted address string or an error
     */
    @SuppressLint("MissingPermission")
    override suspend fun getCurrentAddress(): Result<String> = suspendCancellableCoroutine { continuation ->
        try {
            val locationTask = fusedLocationProviderClient.lastLocation

            locationTask.addOnSuccessListener { location ->
                if (location != null) {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    try {
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                        if (!addresses.isNullOrEmpty()) {
                            val city = addresses[0].locality ?: "Unknown City"
                            val area = addresses[0].featureName ?: "Unknown Area"
                            continuation.resume(Result.success("$area, $city"))
                        } else {
                            continuation.resume(Result.success("Unknown Location"))
                        }
                    } catch (e: Exception) {
                        continuation.resume(Result.failure(e))
                    }
                } else {
                    continuation.resume(Result.success("Unable to fetch location"))
                }
            }

            locationTask.addOnFailureListener { exception ->
                continuation.resume(Result.failure(exception))
            }

            // Register cancellation handler
            continuation.invokeOnCancellation {
                // Handle cancellation if needed
            }
        } catch (e: Exception) {
            continuation.resume(Result.failure(e))
        }
    }

    /**
     * Gets the current location coordinates
     * @return Result containing Pair of latitude and longitude or an error
     */
    @SuppressLint("MissingPermission")
    override suspend fun getCurrentCoordinates(): Result<Pair<Double, Double>> = suspendCancellableCoroutine { continuation ->
        try {
            val locationTask = fusedLocationProviderClient.lastLocation

            locationTask.addOnSuccessListener { location ->
                if (location != null) {
                    continuation.resume(Result.success(Pair(location.latitude, location.longitude)))
                } else {
                    continuation.resume(Result.failure(Exception("Unable to get current location")))
                }
            }

            locationTask.addOnFailureListener { exception ->
                continuation.resume(Result.failure(exception))
            }

            // Register cancellation handler
            continuation.invokeOnCancellation {
                // Handle cancellation if needed
            }
        } catch (e: Exception) {
            continuation.resume(Result.failure(e))
        }
    }
}
