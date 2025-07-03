package com.example.infinite_track.data.soucre.network.response

import com.google.gson.annotations.SerializedName

/**
 * DTO untuk respons Mapbox Geocoding API v6
 * Struktur presisi sesuai dengan respons JSON dari Mapbox
 */
data class MapboxGeocodingResponse(
    @SerializedName("type")
    val type: String,

    @SerializedName("features")
    val features: List<MapboxFeature>,

    @SerializedName("attribution")
    val attribution: String? = null
)

data class MapboxFeature(
    @SerializedName("type")
    val type: String,

    @SerializedName("geometry")
    val geometry: MapboxGeometry,

    @SerializedName("properties")
    val properties: MapboxProperties
)

data class MapboxGeometry(
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

data class MapboxProperties(
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("name_preferred")
    val namePreferred: String? = null,

    @SerializedName("text")
    val text: String? = null,

    @SerializedName("place_name")
    val placeName: String? = null, // Alamat lengkap yang sudah diformat bagus oleh Mapbox

    @SerializedName("full_address")
    val fullAddress: String? = null,

    @SerializedName("place_formatted")
    val placeFormatted: String? = null,

    @SerializedName("context")
    val context: MapboxContext? = null,

    @SerializedName("coordinates")
    val coordinates: MapboxCoordinates? = null
)

data class MapboxContext(
    @SerializedName("country")
    val country: MapboxContextItem? = null,

    @SerializedName("region")
    val region: MapboxContextItem? = null,

    @SerializedName("district")
    val district: MapboxContextItem? = null,

    @SerializedName("locality")
    val locality: MapboxContextItem? = null,

    @SerializedName("place")
    val place: MapboxContextItem? = null,

    @SerializedName("neighborhood")
    val neighborhood: MapboxContextItem? = null,

    @SerializedName("street")
    val street: MapboxContextItem? = null
)

data class MapboxContextItem(
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("country_code")
    val countryCode: String? = null,

    @SerializedName("region_code")
    val regionCode: String? = null
)

data class MapboxCoordinates(
    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double
)
