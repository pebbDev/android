package com.example.infinite_track.domain.use_case.location

import android.annotation.SuppressLint
import com.example.infinite_track.data.soucre.local.room.UserDao
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * Use Case untuk mendapatkan koordinat GPS pengguna saat ini secara real-time
 * Digunakan untuk mendapatkan lokasi terkini, bukan lokasi WFH yang tersimpan
 */
class GetCurrentCoordinatesUseCase @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val userDao: UserDao
) {

    /**
     * Mendapatkan koordinat GPS pengguna saat ini secara real-time
     * Prioritas: GPS Real-time -> WFH dari Database -> Default Jakarta
     * @param useRealTimeGPS jika true, akan mengambil GPS real-time. Jika false, akan mengambil dari database
     * @return Result berisi Pair<latitude, longitude>
     */
    @SuppressLint("MissingPermission")
    suspend operator fun invoke(useRealTimeGPS: Boolean = true): Result<Pair<Double, Double>> {
        return try {
            if (useRealTimeGPS) {
                // Ambil GPS real-time
                getGPSRealTime()
            } else {
                // Ambil dari database (behavior lama)
                getFromDatabase()
            }
        } catch (e: Exception) {
            // Fallback ke database jika GPS gagal
            getFromDatabase()
        }
    }

    /**
     * Mengambil GPS coordinates real-time dari device
     */
    @SuppressLint("MissingPermission")
    private suspend fun getGPSRealTime(): Result<Pair<Double, Double>> =
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
                        val coordinates = Pair(location.latitude, location.longitude)
                        android.util.Log.d(
                            "GetCurrentCoordinatesUseCase",
                            "GPS Real-time coordinates: ${coordinates.first}, ${coordinates.second}"
                        )
                        continuation.resume(Result.success(coordinates))
                    } else {
                        android.util.Log.w(
                            "GetCurrentCoordinatesUseCase",
                            "GPS location is null, falling back to database"
                        )
                        // Fallback ke database
                        CoroutineScope(Dispatchers.IO).launch {
                            val dbResult = getFromDatabase()
                            continuation.resume(dbResult)
                        }
                    }
                }

                locationTask.addOnFailureListener { exception ->
                    android.util.Log.e(
                        "GetCurrentCoordinatesUseCase",
                        "GPS location failed: ${exception.message}"
                    )
                    // Fallback ke database
                    CoroutineScope(Dispatchers.IO).launch {
                        val dbResult = getFromDatabase()
                        continuation.resume(dbResult)
                    }
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
     * Mengambil coordinates dari database (behavior lama)
     */
    private suspend fun getFromDatabase(): Result<Pair<Double, Double>> {
        return try {
            // Coba ambil dari Room Database (lokasi WFH pengguna)
            val userProfile = userDao.getUserProfile()

            if (userProfile?.latitude != null && userProfile.longitude != null) {
                // Gunakan koordinat WFH dari database
                android.util.Log.d(
                    "GetCurrentCoordinatesUseCase",
                    "Using WFH coordinates from database: ${userProfile.latitude}, ${userProfile.longitude}"
                )
                Result.success(Pair(userProfile.latitude, userProfile.longitude))
            } else {
                // Fallback ke koordinat default Jakarta jika tidak ada data WFH
                android.util.Log.d("GetCurrentCoordinatesUseCase", "Using default Jakarta coordinates")
                Result.success(Pair(-6.2088, 106.8456)) // Jakarta coordinates
            }
        } catch (e: Exception) {
            android.util.Log.e(
                "GetCurrentCoordinatesUseCase",
                "Error getting coordinates from database: ${e.message}"
            )
            // Fallback ke koordinat default Jakarta jika terjadi error
            Result.success(Pair(-6.2088, 106.8456)) // Jakarta coordinates
        }
    }
}
