package com.example.infinite_track.data.repository.location

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.infinite_track.BuildConfig
import com.example.infinite_track.data.soucre.network.mapbox.MapboxApiService
import com.example.infinite_track.data.soucre.network.response.MapboxFeature
import com.example.infinite_track.domain.model.location.LocationResult
import com.example.infinite_track.domain.repository.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * Implementation of the LocationRepository interface
 */
class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val mapboxApiService: MapboxApiService
) : LocationRepository {

    /**
     * Gets the current address based on device location using Mapbox Reverse Geocoding API
     * Lebih akurat dan konsisten dibanding android.location.Geocoder
     * @return Result containing the formatted address string or an error
     */
    @SuppressLint("MissingPermission")
    override suspend fun getCurrentAddress(): Result<String> =
        suspendCancellableCoroutine { continuation ->
            try {
                val cancellationTokenSource = CancellationTokenSource()

                // Use getCurrentLocation with high accuracy
                val locationTask = fusedLocationProviderClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                )

                locationTask.addOnSuccessListener { location ->
                    if (location != null) {
                        // Launch coroutine untuk call Mapbox Reverse Geocoding API
                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO)
                            .launch {
                                try {
                                    val response = mapboxApiService.reverseGeocode(
                                        longitude = location.longitude,
                                        latitude = location.latitude,
                                        accessToken = BuildConfig.MAPBOX_PUBLIC_TOKEN
                                    )

                                    // Ekstrak alamat dari response Mapbox
                                    val address = if (response.features.isNotEmpty()) {
                                        val feature = response.features.first()
                                        // Prioritas: fullAddress > placeFormatted > text
                                        feature.properties.fullAddress
                                            ?: feature.properties.placeFormatted
                                            ?: feature.properties.name
                                            ?: "Alamat tidak diketahui"
                                    } else {
                                        "Alamat tidak ditemukan"
                                    }

                                    continuation.resume(Result.success(address))
                                } catch (e: Exception) {
                                    Log.e(
                                        "LocationRepository",
                                        "Mapbox reverse geocoding failed",
                                        e
                                    )
                                    // Fallback ke alamat sederhana dengan koordinat
                                    val fallbackAddress =
                                        "Lat: ${location.latitude}, Lng: ${location.longitude}"
                                    continuation.resume(Result.success(fallbackAddress))
                                }
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

    /**
     * Search for locations based on query text using Mapbox API
     * @param query Search query
     * @param userLatitude User's current latitude for proximity search (optional)
     * @param userLongitude User's current longitude for proximity search (optional)
     * @return Result containing list of LocationResult or error
     */
    override suspend fun searchLocation(
        query: String,
        userLatitude: Double?,
        userLongitude: Double?
    ): Result<List<LocationResult>> {
        return try {
            // Buat proximity string jika koordinat pengguna tersedia
            val proximity = if (userLatitude != null && userLongitude != null) {
                "$userLongitude,$userLatitude"
            } else null

            // Panggil Mapbox API langsung melalui MapboxApiService terpisah
            val response = mapboxApiService.searchLocation(
                searchQuery = query,
                accessToken = BuildConfig.MAPBOX_PUBLIC_TOKEN,
                proximity = proximity,
                limit = 10
            )

            // Transform DTO ke domain model
            val locationResults = response.features.mapNotNull { feature ->
                feature.toLocationResult()
            }

            Result.success(locationResults)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Extension function untuk mentransformasi MapboxFeature ke LocationResult
     * Disederhanakan untuk memanfaatkan pemformatan alamat dari Mapbox yang sudah bagus
     */
    private fun MapboxFeature.toLocationResult(): LocationResult? {
        return try {
            // 'text' biasanya berisi nama utama tempat (misal: "Plaza Indonesia")
            // 'placeName' berisi alamat lengkap yang sudah diformat bagus oleh Mapbox
            LocationResult(
                placeName = this.properties.text
                    ?: this.properties.name
                    ?: this.properties.namePreferred
                    ?: "Unknown Location",
                address = this.properties.placeName
                    ?: this.properties.fullAddress
                    ?: this.properties.placeFormatted
                    ?: "Address not available",
                latitude = this.geometry.latitude,
                longitude = this.geometry.longitude
            )
        } catch (e: Exception) {
            null // Skip jika ada error dalam transformasi
        }
    }
}