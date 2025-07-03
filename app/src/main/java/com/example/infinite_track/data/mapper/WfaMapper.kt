package com.example.infinite_track.data.mapper

import com.example.infinite_track.data.soucre.network.response.RecommendationItem
import com.example.infinite_track.domain.model.wfa.WfaRecommendation

/**
 * Mapper functions to convert network DTOs to domain models
 */

fun RecommendationItem.toDomain(): WfaRecommendation {
    return WfaRecommendation(
        name = this.name,
        address = this.address,
        latitude = this.latitude,
        longitude = this.longitude,
        score = this.suitabilityScore,
        label = this.suitabilityLabel,
        category = this.category,
        distance = this.distanceFromCenter / 1000.0
    )
}

fun List<RecommendationItem>.toDomain(): List<WfaRecommendation> {
    return this.map { it.toDomain() }
}
