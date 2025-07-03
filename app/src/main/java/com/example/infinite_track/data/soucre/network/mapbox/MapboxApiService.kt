package com.example.infinite_track.data.soucre.network.mapbox

import com.example.infinite_track.data.soucre.network.response.MapboxGeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface khusus untuk Mapbox Geocoding API v6
 * Menggunakan base URL Mapbox langsung: https://api.mapbox.com/
 */
interface MapboxApiService {

    /**
     * Search location menggunakan Mapbox Geocoding API v6
     * URL: https://api.mapbox.com/search/geocode/v6/forward
     */
    @GET("search/geocode/v6/forward")
    suspend fun searchLocation(
        @Query("q") searchQuery: String,
        @Query("access_token") accessToken: String,
        @Query("limit") limit: Int = 10,
        @Query("proximity") proximity: String? = null,
        @Query("country") country: String = "ID",
        @Query("types") types: String = "place,address,locality,neighborhood",
        @Query("language") language: String = "id"
    ): MapboxGeocodingResponse


    @GET("search/geocode/v6/reverse")
    suspend fun reverseGeocode(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("access_token") accessToken: String,
        @Query("types") types: String = "place,address,locality,neighborhood",
        @Query("limit") limit: Int = 1
    ): MapboxGeocodingResponse
}
