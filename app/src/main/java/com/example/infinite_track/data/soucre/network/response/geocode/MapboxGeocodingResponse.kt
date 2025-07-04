package com.example.infinite_track.data.soucre.network.response.geocode

import com.google.gson.annotations.SerializedName

/**
 * DTO untuk respons Mapbox Geocoding API v6
 * Struktur khusus untuk reverse geocoding (koordinat → alamat)
 */
data class MapboxGeocodingResponse(
    @SerializedName("type")
    val type: String,

    @SerializedName("features")
    val features: List<MapboxGeocodingFeature>,

    @SerializedName("attribution")
    val attribution: String? = null
)

data class MapboxGeocodingFeature(
    @SerializedName("type")
    val type: String,

    @SerializedName("geometry")
    val geometry: MapboxGeocodingGeometry,

    @SerializedName("properties")
    val properties: MapboxGeocodingProperties
)

data class MapboxGeocodingGeometry(
    @SerializedName("type")
    val type: String,

    @SerializedName("coordinates")
    val coordinates: List<Double>
) {
    // Helper untuk mendapatkan longitude (index 0)
    val longitude: Double get() = coordinates[0]

    // Helper untuk mendapatkan latitude (index 1)
    val latitude: Double get() = coordinates[1]
}

data class MapboxGeocodingProperties(
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("name_preferred")
    val namePreferred: String? = null,

    @SerializedName("text")
    val text: String? = null,

    @SerializedName("place_name")
    val placeName: String? = null,

    @SerializedName("full_address")
    val fullAddress: String? = null,

    @SerializedName("place_formatted")
    val placeFormatted: String? = null,

    @SerializedName("context")
    val context: MapboxGeocodingContext? = null,

    @SerializedName("coordinates")
    val coordinates: MapboxGeocodingCoordinates? = null
)

data class MapboxGeocodingContext(
    @SerializedName("country")
    val country: MapboxGeocodingContextItem? = null,

    @SerializedName("region")
    val region: MapboxGeocodingContextItem? = null,

    @SerializedName("district")
    val district: MapboxGeocodingContextItem? = null,

    @SerializedName("locality")
    val locality: MapboxGeocodingContextItem? = null,

    @SerializedName("place")
    val place: MapboxGeocodingContextItem? = null,

    @SerializedName("neighborhood")
    val neighborhood: MapboxGeocodingContextItem? = null,

    @SerializedName("street")
    val street: MapboxGeocodingContextItem? = null,

    @SerializedName("postcode")
    val postcode: MapboxGeocodingContextItem? = null
)

data class MapboxGeocodingContextItem(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("country_code")
    val countryCode: String? = null,

    @SerializedName("region_code")
    val regionCode: String? = null
)

data class MapboxGeocodingCoordinates(
    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double
)
