package com.example.infinite_track.data.soucre.network.response.search

import com.google.gson.annotations.SerializedName

/**
 * DTO untuk respons Mapbox Search Box API v1 Forward endpoint
 * Struktur mengikuti GeoJSON FeatureCollection untuk forward search
 */
data class MapboxSearchResponse(
    @SerializedName("type")
    val type: String, // "FeatureCollection"

    @SerializedName("features")
    val features: List<MapboxSearchFeature>,

    @SerializedName("attribution")
    val attribution: String? = null
)

data class MapboxSearchFeature(
    @SerializedName("type")
    val type: String, // "Feature"

    @SerializedName("geometry")
    val geometry: MapboxSearchGeometry,

    @SerializedName("properties")
    val properties: MapboxSearchProperties
)

data class MapboxSearchGeometry(
    @SerializedName("type")
    val type: String, // "Point"

    @SerializedName("coordinates")
    val coordinates: List<Double> // [longitude, latitude]
) {
    // Helper untuk mendapatkan longitude (index 0)
    val longitude: Double get() = coordinates[0]

    // Helper untuk mendapatkan latitude (index 1)
    val latitude: Double get() = coordinates[1]
}

data class MapboxSearchProperties(
    @SerializedName("name")
    val name: String,

    @SerializedName("mapbox_id")
    val mapboxId: String? = null,

    @SerializedName("feature_type")
    val featureType: String? = null,

    @SerializedName("address")
    val address: String? = null,

    @SerializedName("full_address")
    val fullAddress: String? = null,

    @SerializedName("place_formatted")
    val placeFormatted: String? = null,

    @SerializedName("context")
    val context: MapboxSearchContext? = null,

    @SerializedName("coordinates")
    val coordinatesProperty: MapboxSearchCoordinates? = null,

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
    val distance: Long? = null
)

data class MapboxSearchCoordinates(
    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,

    @SerializedName("accuracy")
    val accuracy: String? = null,

    @SerializedName("routable_points")
    val routablePoints: List<MapboxRoutablePoint>? = null
)

data class MapboxRoutablePoint(
    @SerializedName("name")
    val name: String,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double
)

data class MapboxExternalIds(
    @SerializedName("foursquare")
    val foursquare: String? = null,

    @SerializedName("safegraph")
    val safegraph: String? = null
)

data class MapboxSearchMetadata(
    @SerializedName("iso_3166_1")
    val iso31661: String? = null,

    @SerializedName("iso_3166_2")
    val iso31662: String? = null,

    @SerializedName("primary_photo")
    val primaryPhoto: List<String>? = null
)

data class MapboxSearchContext(
    @SerializedName("country")
    val country: MapboxSearchContextItem? = null,

    @SerializedName("region")
    val region: MapboxSearchContextItem? = null,

    @SerializedName("postcode")
    val postcode: MapboxSearchContextItem? = null,

    @SerializedName("district")
    val district: MapboxSearchContextItem? = null,

    @SerializedName("place")
    val place: MapboxSearchContextItem? = null,

    @SerializedName("locality")
    val locality: MapboxSearchContextItem? = null,

    @SerializedName("neighborhood")
    val neighborhood: MapboxSearchContextItem? = null,

    @SerializedName("street")
    val street: MapboxSearchContextItem? = null,

    @SerializedName("address")
    val address: MapboxSearchAddressContext? = null
)

data class MapboxSearchContextItem(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("country_code")
    val countryCode: String? = null,

    @SerializedName("country_code_alpha_3")
    val countryCodeAlpha3: String? = null,

    @SerializedName("region_code")
    val regionCode: String? = null,

    @SerializedName("region_code_full")
    val regionCodeFull: String? = null
)

data class MapboxSearchAddressContext(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("address_number")
    val addressNumber: String? = null,

    @SerializedName("street_name")
    val streetName: String? = null
)
