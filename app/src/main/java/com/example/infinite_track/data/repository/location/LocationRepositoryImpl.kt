package com.example.infinite_track.data.repository.location

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.infinite_track.BuildConfig
import com.example.infinite_track.data.soucre.network.retrofit.MapboxApiService
import com.example.infinite_track.data.soucre.network.response.search.MapboxSearchFeature
import com.example.infinite_track.domain.model.location.LocationResult
import com.example.infinite_track.domain.repository.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * Implementation of the LocationRepository interface
 * Menggunakan Mapbox Search Box API untuk pencarian POI dan Geocoding API untuk reverse geocoding
 */
class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val mapboxApiService: MapboxApiService
) : LocationRepository {

    companion object {
        private const val TAG = "LocationRepository"
    }

    /**
     * Gets the current address based on device location using Mapbox Reverse Geocoding API
     * Menggunakan Geocoding API v6 yang tepat untuk reverse geocoding
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
                        // Launch coroutine untuk call Mapbox Reverse Geocoding API yang benar
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                Log.d(TAG, "Calling reverse geocode for: ${location.latitude}, ${location.longitude}")

                                val response = mapboxApiService.reverseGeocode(
                                    longitude = location.longitude,
                                    latitude = location.latitude,
                                    accessToken = BuildConfig.MAPBOX_PUBLIC_TOKEN
                                )

                                // Ekstrak alamat dari response Geocoding API
                                val address = if (response.features.isNotEmpty()) {
                                    val feature = response.features.first()
                                    // Prioritas: fullAddress > placeFormatted > placeName > text
                                    feature.properties.fullAddress
                                        ?: feature.properties.placeFormatted
                                        ?: feature.properties.placeName
                                        ?: feature.properties.text
                                        ?: "Alamat tidak diketahui"
                                } else {
                                    "Alamat tidak ditemukan"
                                }

                                Log.d(TAG, "Reverse geocode success: $address")
                                if (continuation.isActive) {
                                    continuation.resume(Result.success(address))
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "Mapbox reverse geocoding failed", e)
                                // Fallback ke alamat sederhana dengan koordinat
                                val fallbackAddress = "Lat: ${location.latitude}, Lng: ${location.longitude}"
                                if (continuation.isActive) {
                                    continuation.resume(Result.success(fallbackAddress))
                                }
                            }
                        }
                    } else {
                        Log.w(TAG, "Location is null")
                        continuation.resume(Result.success("Unable to fetch location"))
                    }
                }

                locationTask.addOnFailureListener { exception ->
                    Log.e(TAG, "Failed to get current location", exception)
                    continuation.resume(Result.failure(exception))
                }

                // Register cancellation handler
                continuation.invokeOnCancellation {
                    cancellationTokenSource.cancel()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error in getCurrentAddress", e)
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
     * Search for POI, places, cafes, hotels, etc using Mapbox Search Box API v1 Forward
     * Menggunakan Forward endpoint yang memberikan koordinat lengkap langsung
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
            Log.d(TAG, "Searching for places with query: $query")

            // Buat proximity string jika koordinat pengguna tersedia
            val proximity = if (userLatitude != null && userLongitude != null) {
                "$userLongitude,$userLatitude"
            } else null

            Log.d(TAG, "Using proximity: $proximity")

            // PERBAIKAN: Gunakan endpoint /forward untuk mendapatkan koordinat lengkap
            val response = mapboxApiService.searchPlace(
                searchQuery = query,
                accessToken = BuildConfig.MAPBOX_PUBLIC_TOKEN,
                proximity = proximity,
                poiCategory = when {
                    query.contains("hotel", ignoreCase = true) -> "hotel"
                    query.contains("cafe", ignoreCase = true) -> "cafe"
                    query.contains("restaurant", ignoreCase = true) -> "restaurant"
                    query.contains("mall", ignoreCase = true) -> "shopping"
                    else -> null
                }
            )

            Log.d(TAG, "Search Box API Forward response: ${response.features.size} features")

            // Transform GeoJSON features ke domain model
            val locationResults = response.features.mapNotNull { feature ->
                try {
                    LocationResult(
                        placeName = feature.properties.name,
                        address = feature.properties.fullAddress
                            ?: feature.properties.placeFormatted
                            ?: feature.properties.address
                            ?: buildContextualAddress(feature)
                            ?: "Address not available",
                        latitude = feature.geometry.latitude,
                        longitude = feature.geometry.longitude
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to convert feature to LocationResult: ${feature.properties.name}", e)
                    null
                }
            }

            Log.d(TAG, "Successfully converted ${locationResults.size} location results")
            locationResults.forEachIndexed { index, location ->
                Log.d(TAG, "  [$index] ${location.placeName} - ${location.address}")
            }

            Result.success(locationResults)

        } catch (e: Exception) {
            Log.e(TAG, "Mapbox search failed", e)
            Result.failure(e)
        }
    }

    /**
     * Helper untuk membangun alamat dari context jika fullAddress tidak tersedia
     */
    private fun buildContextualAddress(feature: MapboxSearchFeature): String? {
        return try {
            val parts = mutableListOf<String>()

            feature.properties.context?.street?.name?.let { parts.add(it) }
            feature.properties.context?.neighborhood?.name?.let { parts.add(it) }
            feature.properties.context?.locality?.name?.let { parts.add(it) }
            feature.properties.context?.place?.name?.let { parts.add(it) }
            feature.properties.context?.region?.name?.let { parts.add(it) }
            feature.properties.context?.country?.name?.let { parts.add(it) }

            if (parts.isNotEmpty()) {
                parts.joinToString(", ")
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to build contextual address", e)
            null
        }
    }
}