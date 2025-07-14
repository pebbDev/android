package com.example.infinite_track.data.repository.wfa

import com.example.infinite_track.data.mapper.wfa.toDomain
import com.example.infinite_track.data.soucre.network.retrofit.ApiService
import com.example.infinite_track.domain.model.wfa.WfaRecommendation
import com.example.infinite_track.domain.repository.WfaRepository
import javax.inject.Inject

/**
 * Implementation of WfaRepository that fetches data from network API
 */
class WfaRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : WfaRepository {

    override suspend fun getRecommendations(
        latitude: Double,
        longitude: Double
    ): Result<List<WfaRecommendation>> {
        return try {
            val response = apiService.getWfaRecommendations(latitude, longitude)
            if (response.success) {
                Result.success(response.data.recommendations.toDomain())
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}