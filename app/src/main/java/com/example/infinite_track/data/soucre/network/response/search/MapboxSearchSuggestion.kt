package com.example.infinite_track.data.soucre.network.response.search

import com.google.gson.annotations.SerializedName

/**
 * DTO untuk respons Mapbox Search Box API v1 Suggest endpoint
 * Struktur untuk suggestions yang tidak memiliki koordinat
 */
data class MapboxSuggestResponse(
    @SerializedName("suggestions")
    val suggestions: List<MapboxSearchSuggestion>,

    @SerializedName("attribution")
    val attribution: String? = null,

    @SerializedName("response_id")
    val responseId: String? = null
)

data class MapboxSearchSuggestion(
    @SerializedName("name")
    val name: String,

    @SerializedName("mapbox_id")
    val mapboxId: String,

    @SerializedName("feature_type")
    val featureType: String,

    @SerializedName("address")
    val address: String? = null,

    @SerializedName("full_address")
    val fullAddress: String? = null,

    @SerializedName("place_formatted")
    val placeFormatted: String? = null,

    @SerializedName("context")
    val context: MapboxSearchContext? = null,

    @SerializedName("language")
    val language: String? = null,

    @SerializedName("maki")
    val maki: String? = null,

    @SerializedName("poi_category")
    val poiCategory: List<String>? = null,

    @SerializedName("poi_category_ids")
    val poiCategoryIds: List<String>? = null,

    @SerializedName("brand")
    val brand: List<String>? = null,

    @SerializedName("brand_id")
    val brandId: List<String>? = null,

    @SerializedName("external_ids")
    val externalIds: MapboxExternalIds? = null,

    @SerializedName("metadata")
    val metadata: MapboxSearchMetadata? = null,

    @SerializedName("distance")
    val distance: Long? = null,

    // Koordinat tidak tersedia di suggest endpoint
    @SerializedName("coordinate")
    val coordinate: MapboxSearchCoordinates? = null
)
