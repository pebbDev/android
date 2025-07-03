package com.example.infinite_track.domain.repository

import com.example.infinite_track.domain.model.wfa.WfaRecommendation

/**
 * Repository interface for WFA (Work From Anywhere) recommendations
 */
interface WfaRepository {
    suspend fun getRecommendations(
        latitude: Double,
        longitude: Double
    ): Result<List<WfaRecommendation>>
}
