package com.example.infinite_track.domain.use_case.location

import com.example.infinite_track.data.soucre.local.room.UserDao
import javax.inject.Inject

/**
 * Use Case untuk mendapatkan koordinat pengguna saat ini
 * Digunakan untuk proximity search dalam pencarian lokasi
 * Default mengambil dari lokasi WFH yang tersimpan di Room Database
 */
class GetCurrentCoordinatesUseCase @Inject constructor(
    private val userDao: UserDao
) {

    /**
     * Mendapatkan koordinat pengguna saat ini
     * Prioritas: Lokasi WFH dari Room Database -> Default Jakarta
     * @return Result berisi Pair<latitude, longitude>
     */
    suspend operator fun invoke(): Result<Pair<Double, Double>> {
        return try {
            // Coba ambil dari Room Database (lokasi WFH pengguna)
            val userProfile = userDao.getUserProfile()

            if (userProfile?.latitude != null && userProfile.longitude != null) {
                // Gunakan koordinat WFH dari database
                Result.success(Pair(userProfile.latitude, userProfile.longitude))
            } else {
                // Fallback ke koordinat default Jakarta jika tidak ada data WFH
                Result.success(Pair(-6.2088, 106.8456)) // Jakarta coordinates
            }
        } catch (e: Exception) {
            // Fallback ke koordinat default Jakarta jika terjadi error
            Result.success(Pair(-6.2088, 106.8456)) // Jakarta coordinates
        }
    }
}
