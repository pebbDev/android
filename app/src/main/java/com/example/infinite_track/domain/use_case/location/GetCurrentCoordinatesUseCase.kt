package com.example.infinite_track.domain.use_case.location

import android.annotation.SuppressLint
import com.example.infinite_track.data.soucre.local.room.UserDao
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * Use Case untuk mendapatkan koordinat GPS pengguna dengan logika fallback yang pragmatis
 * Mendukung dua mode: GPS real-time atau WFH dari database
 */
class GetCurrentCoordinatesUseCase @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val userDao: UserDao
) {

    /**
     * Mendapatkan koordinat pengguna dengan logika fallback
     * @param useRealTimeGPS jika true, akan mengambil GPS real-time. Jika false, akan mengambil dari database
     * @return Result berisi Pair<latitude, longitude>
     */
    @SuppressLint("MissingPermission")
    suspend operator fun invoke(useRealTimeGPS: Boolean = true): Result<Pair<Double, Double>> {
        return try {
            if (useRealTimeGPS) {
                // Coba GPS real-time dulu
                val gpsResult = getGPSRealTime()
                if (gpsResult.isSuccess) {
                    gpsResult
                } else {
                    // Jika GPS gagal, fallback ke database
                    android.util.Log.w(
                        "GetCurrentCoordinatesUseCase",
                        "GPS failed, falling back to WFH location from database"
                    )
                    getFromDatabase()
                }
            } else {
                // Langsung ambil dari database
                getFromDatabase()
            }
        } catch (e: Exception) {
            android.util.Log.e(
                "GetCurrentCoordinatesUseCase",
                "Error getting coordinates: ${e.message}"
            )
            Result.failure(e)
        }
    }

    /**
     * Mengambil GPS coordinates real-time dari device
     * Tidak ada fallback internal - jika gagal akan return Result.failure
     */
    @SuppressLint("MissingPermission")
    private suspend fun getGPSRealTime(): Result<Pair<Double, Double>> =
        suspendCancellableCoroutine { continuation ->
            try {
                val cancellationTokenSource = CancellationTokenSource()

                val locationTask = fusedLocationProviderClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                )

                locationTask.addOnSuccessListener { location ->
                    if (location != null) {
                        val coordinates = Pair(location.latitude, location.longitude)
                        android.util.Log.d(
                            "GetCurrentCoordinatesUseCase",
                            "GPS Real-time coordinates: ${coordinates.first}, ${coordinates.second}"
                        )
                        continuation.resume(Result.success(coordinates))
                    } else {
                        android.util.Log.w(
                            "GetCurrentCoordinatesUseCase",
                            "GPS location is null"
                        )
                        continuation.resume(Result.failure(Exception("GPS location is null")))
                    }
                }

                locationTask.addOnFailureListener { exception ->
                    android.util.Log.e(
                        "GetCurrentCoordinatesUseCase",
                        "GPS location failed: ${exception.message}"
                    )
                    continuation.resume(Result.failure(exception))
                }

                // Handle cancellation
                continuation.invokeOnCancellation {
                    cancellationTokenSource.cancel()
                }

            } catch (e: Exception) {
                android.util.Log.e(
                    "GetCurrentCoordinatesUseCase",
                    "Error getting GPS location: ${e.message}"
                )
                continuation.resume(Result.failure(e))
            }
        }

    /**
     * Mengambil coordinates WFH dari database
     * Jika tidak ada data WFH, akan return Result.failure
     */
    private suspend fun getFromDatabase(): Result<Pair<Double, Double>> {
        return try {
            // Ambil dari Room Database (lokasi WFH pengguna)
            val userProfile = userDao.getUserProfile()

            if (userProfile?.latitude != null && userProfile.longitude != null) {
                // Gunakan koordinat WFH dari database
                android.util.Log.d(
                    "GetCurrentCoordinatesUseCase",
                    "Using WFH coordinates from database: ${userProfile.latitude}, ${userProfile.longitude}"
                )
                Result.success(Pair(userProfile.latitude, userProfile.longitude))
            } else {
                // Tidak ada data WFH - return failure
                android.util.Log.w(
                    "GetCurrentCoordinatesUseCase",
                    "No WFH location data found in database"
                )
                Result.failure(Exception("No WFH location data found. Please set your work from home location."))
            }
        } catch (e: Exception) {
            android.util.Log.e(
                "GetCurrentCoordinatesUseCase",
                "Error getting coordinates from database: ${e.message}"
            )
            Result.failure(e)
        }
    }
}
