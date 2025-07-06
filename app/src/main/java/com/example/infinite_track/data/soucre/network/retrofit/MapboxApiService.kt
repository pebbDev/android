package com.example.infinite_track.data.soucre.network.retrofit

import com.example.infinite_track.data.soucre.network.response.geocode.MapboxGeocodingResponse
import com.example.infinite_track.data.soucre.network.response.search.MapboxSearchResponse
import com.example.infinite_track.data.soucre.network.response.search.MapboxSuggestResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface untuk Mapbox APIs
 * Menggunakan tiga endpoint berbeda untuk kebutuhan yang berbeda:
 * 1. Search Box API v1 Forward - untuk mendapatkan koordinat lengkap
 * 2. Search Box API v1 Suggest - untuk autocomplete (tidak memiliki koordinat)
 * 3. Geocoding API v6 - untuk reverse geocoding (koordinat → alamat)
 */
interface MapboxApiService {

    /**
     * Search dengan koordinat lengkap menggunakan Mapbox Search Box API v1 Forward
     * Endpoint: https://api.mapbox.com/search/searchbox/v1/forward
     * Forward endpoint memberikan koordinat lengkap dalam format GeoJSON
     */
    @GET("search/searchbox/v1/forward")
    suspend fun searchPlace(
        @Query("q") searchQuery: String,
        @Query("access_token") accessToken: String,
        @Query("language") language: String = "id",
        @Query("country") country: String = "ID",
        @Query("proximity") proximity: String? = null,
        @Query("limit") limit: Int = 10,
        @Query("types") types: String = "poi,address", // Focus on POI and addresses
        @Query("poi_category") poiCategory: String? = null // Untuk filter kategori POI
    ): MapboxSearchResponse

    /**
     * Autocomplete suggestions menggunakan Mapbox Search Box API v1 Suggest
     * Endpoint: https://api.mapbox.com/search/searchbox/v1/suggest
     * Suggest endpoint untuk autocomplete, tidak memiliki koordinat
     */
    @GET("search/searchbox/v1/suggest")
    suspend fun getSuggestions(
        @Query("q") searchQuery: String,
        @Query("access_token") accessToken: String,
        @Query("session_token") sessionToken: String,
        @Query("language") language: String = "id",
        @Query("country") country: String = "ID",
        @Query("proximity") proximity: String? = null,
        @Query("limit") limit: Int = 10
    ): MapboxSuggestResponse

    /**
     * Reverse geocoding menggunakan Mapbox Geocoding API v6
     * Endpoint: https://api.mapbox.com/search/geocode/v6/reverse
     * Mengkonversi koordinat menjadi alamat
     */
    @GET("search/geocode/v6/reverse")
    suspend fun reverseGeocode(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("access_token") accessToken: String,
        @Query("language") language: String = "id",
        @Query("country") country: String = "ID",
        @Query("limit") limit: Int = 1
    ): MapboxGeocodingResponse

    /**
     * Geocoding untuk mendapatkan alamat dari koordinat
     * Endpoint: https://api.mapbox.com/search/geocode/v6/forward
     * Menggunakan Geocoding API v6 untuk mendapatkan alamat lengkap
     */
    @GET("search/geocode/v6/forward")
    suspend fun geocodePlace(
        @Query("q") searchQuery: String,
        @Query("access_token") accessToken: String,
        @Query("language") language: String = "id",
        @Query("country") country: String = "ID",
        @Query("proximity") proximity: String? = null,
        @Query("limit") limit: Int = 10
    ): MapboxGeocodingResponse
}