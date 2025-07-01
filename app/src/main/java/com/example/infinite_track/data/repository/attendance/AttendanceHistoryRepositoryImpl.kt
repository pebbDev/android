package com.example.infinite_track.data.repository.attendance

import android.util.Log
import com.example.infinite_track.data.mapper.attendance.toDomain
import com.example.infinite_track.data.soucre.network.response.ErrorResponse
import com.example.infinite_track.data.soucre.network.retrofit.ApiService
import com.example.infinite_track.domain.model.attendance.AttendanceHistoryPage
import com.example.infinite_track.domain.repository.AttendanceHistoryRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttendanceHistoryRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AttendanceHistoryRepository {

    override suspend fun getAttendanceHistory(
        period: String,
        page: Int,
        limit: Int
    ): Result<AttendanceHistoryPage> = withContext(Dispatchers.IO) {
        try {
            // Fetch attendance history from the API
            val response = apiService.getAttendanceHistory(period, page, limit)

            if (response.success) {
                // Map the response data to domain model
                val attendanceHistoryPage = response.data.toDomain()
                return@withContext Result.success(attendanceHistoryPage)
            } else {
                // Handle unsuccessful response
                return@withContext Result.failure(Exception(response.message))
            }
        } catch (e: HttpException) {
            // Handle HTTP errors
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody?.message ?: "Unknown error from server"
            Log.e("AttendanceHistoryRepo", "HTTP Error: $errorMessage", e)
            return@withContext Result.failure(Exception(errorMessage))
        } catch (e: IOException) {
            // Handle network errors
            Log.e("AttendanceHistoryRepo", "Network Error", e)
            return@withContext Result.failure(Exception("Network error, please check your internet connection."))
        } catch (e: Exception) {
            // Handle unexpected errors
            Log.e("AttendanceHistoryRepo", "Unknown Error", e)
            return@withContext Result.failure(e)
        }
    }
}
