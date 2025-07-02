package com.example.infinite_track.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.example.infinite_track.domain.repository.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
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
     * Gets the current address based on device location with high accuracy
     * @return Result containing the formatted address string or an error
     */
    @SuppressLint("MissingPermission")
    override suspend fun getCurrentAddress(): Result<String> =
        suspendCancellableCoroutine { continuation ->
            try {
                val cancellationTokenSource = CancellationTokenSource()

                // Use getCurrentLocation with high accuracy instead of lastLocation
                val locationTask = fusedLocationProviderClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                )

                locationTask.addOnSuccessListener { location ->
                    if (location != null) {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        try {
                            val addresses =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)

                            if (!addresses.isNullOrEmpty()) {
                                val address = addresses[0]
                                val streetNumber = address.subThoroughfare ?: ""
                                val streetName = address.thoroughfare ?: ""
                                val subLocality = address.subLocality ?: ""
                                val locality = address.locality ?: ""
                                val adminArea = address.adminArea ?: ""

                                // Build formatted address string
                                val addressParts = mutableListOf<String>()

                                // Street address (Jl. Name + Number)
                                if (streetName.isNotEmpty()) {
                                    val streetAddress = if (streetNumber.isNotEmpty()) {
                                        "$streetName No. $streetNumber"
                                    } else {
                                        streetName
                                    }
                                    addressParts.add(streetAddress)
                                }

                                // Sub-locality (Kelurahan/Area)
                                if (subLocality.isNotEmpty() && subLocality != locality) {
                                    addressParts.add(subLocality)
                                }

                                // Locality (City/Kota)
                                if (locality.isNotEmpty()) {
                                    addressParts.add(locality)
                                }

                                // Admin Area (Province/Provinsi)
                                if (adminArea.isNotEmpty()) {
                                    addressParts.add(adminArea)
                                }

                                val formattedAddress = if (addressParts.isNotEmpty()) {
                                    addressParts.joinToString(", ")
                                } else {
                                    "Unknown Location"
                                }

                                continuation.resume(Result.success(formattedAddress))
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
                    cancellationTokenSource.cancel()
                }
            } catch (e: Exception) {
                continuation.resume(Result.failure(e))
            }
        }

    /**
     * Gets the current location coordinates with high accuracy
     * @return Result containing Pair of latitude and longitude or an error
     */
    @SuppressLint("MissingPermission")
    override suspend fun getCurrentCoordinates(): Result<Pair<Double, Double>> =
        suspendCancellableCoroutine { continuation ->
            try {
                val cancellationTokenSource = CancellationTokenSource()

                // Use getCurrentLocation with high accuracy instead of lastLocation
                val locationTask = fusedLocationProviderClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                )

                locationTask.addOnSuccessListener { location ->
                    if (location != null) {
                        continuation.resume(
                            Result.success(
                                Pair(
                                    location.latitude,
                                    location.longitude
                                )
                            )
                        )
                    } else {
                        continuation.resume(Result.failure(Exception("Unable to get current location")))
                    }
                }

                locationTask.addOnFailureListener { exception ->
                    continuation.resume(Result.failure(exception))
                }

                // Register cancellation handler
                continuation.invokeOnCancellation {
                    cancellationTokenSource.cancel()
                }
            } catch (e: Exception) {
                continuation.resume(Result.failure(e))
            }
        }
}

