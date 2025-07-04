package com.example.infinite_track.presentation.screen.attendance.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infinite_track.domain.model.location.LocationResult
import com.example.infinite_track.domain.use_case.location.GetCurrentCoordinatesUseCase
import com.example.infinite_track.domain.use_case.location.SearchLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel untuk pencarian lokasi dengan debouncing
 * Mengelola state pencarian dan komunikasi dengan domain layer
 */
@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchLocationUseCase: SearchLocationUseCase,
    private val getCurrentCoordinatesUseCase: GetCurrentCoordinatesUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "SearchViewModel"
        private const val DEBOUNCE_DELAY = 500L // 500ms debouncing
        private const val MIN_QUERY_LENGTH = 2 // Minimal 2 karakter untuk search
    }

    // Search query state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Search UI state
    private val _searchState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val searchState: StateFlow<SearchUiState> = _searchState.asStateFlow()

    // Current user coordinates for proximity search
    private var userLatitude: Double? = null
    private var userLongitude: Double? = null

    private var searchJob: Job? = null

    init {
        setupSearchFlow()
        getCurrentLocation()
    }

    /**
     * Setup search flow dengan debouncing
     */
    private fun setupSearchFlow() {
        _searchQuery
            .debounce(DEBOUNCE_DELAY)
            .distinctUntilChanged()
            .filter { query ->
                if (query.isBlank()) {
                    _searchState.value = SearchUiState.Idle
                    false
                } else if (query.length < MIN_QUERY_LENGTH) {
                    _searchState.value = SearchUiState.Idle
                    false
                } else {
                    true
                }
            }
            .onEach { query ->
                performSearch(query)
            }
            .launchIn(viewModelScope)
    }

    /**
     * Update search query
     */
    fun updateSearchQuery(query: String) {
        Log.d(TAG, "Search query updated: $query")
        _searchQuery.value = query
    }

    /**
     * Get current user location for proximity search
     * Menggunakan GPS real-time untuk proximity search yang akurat
     */
    private fun getCurrentLocation() {
        viewModelScope.launch {
            try {
                // PENTING: Gunakan GPS real-time untuk proximity search
                getCurrentCoordinatesUseCase(useRealTimeGPS = true).onSuccess { coordinates ->
                    val (latitude, longitude) = coordinates
                    userLatitude = latitude
                    userLongitude = longitude
                    Log.d(TAG, "User GPS real-time location updated for search proximity: $latitude, $longitude")
                }.onFailure { exception ->
                    Log.w(TAG, "Failed to get GPS real-time location, trying cached location", exception)
                    // Fallback ke cached location
                    getCurrentCoordinatesUseCase(useRealTimeGPS = false).onSuccess { coordinates ->
                        val (latitude, longitude) = coordinates
                        userLatitude = latitude
                        userLongitude = longitude
                        Log.d(TAG, "Using cached location for search proximity: $latitude, $longitude")
                    }.onFailure { fallbackException ->
                        Log.w(TAG, "Failed to get any location for proximity search", fallbackException)
                        // Continue without proximity search
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "Unexpected error getting user location", e)
            }
        }
    }

    /**
     * Perform search with current query
     */
    private fun performSearch(query: String) {
        // Cancel previous search
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            try {
                Log.d(TAG, "Starting search for: $query")
                _searchState.value = SearchUiState.Loading

                // Perform search with proximity if available
                searchLocationUseCase(
                    query = query,
                    userLatitude = userLatitude,
                    userLongitude = userLongitude
                ).onSuccess { locations ->
                    Log.d(TAG, "Search completed: ${locations.size} results found")

                    _searchState.value = if (locations.isEmpty()) {
                        SearchUiState.Empty
                    } else {
                        SearchUiState.Success(locations)
                    }
                }.onFailure { exception ->
                    Log.e(TAG, "Search failed", exception)
                    _searchState.value = SearchUiState.Error(
                        exception.message ?: "Pencarian gagal. Silakan coba lagi."
                    )
                }

            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error in search", e)
                _searchState.value = SearchUiState.Error(
                    "Terjadi kesalahan tidak terduga. Silakan coba lagi."
                )
            }
        }
    }

    /**
     * Clear search results and query
     */
    fun clearSearch() {
        Log.d(TAG, "Clearing search")
        searchJob?.cancel()
        _searchQuery.value = ""
        _searchState.value = SearchUiState.Idle
    }

    /**
     * Retry last search
     */
    fun retrySearch() {
        val currentQuery = _searchQuery.value
        if (currentQuery.isNotBlank() && currentQuery.length >= MIN_QUERY_LENGTH) {
            Log.d(TAG, "Retrying search for: $currentQuery")
            performSearch(currentQuery)
        }
    }

    /**
     * Handle location selection
     */
    fun onLocationSelected(location: LocationResult) {
        Log.d(TAG, "Location selected: ${location.placeName}")
        // This will be handled by the parent ViewModel or UI
        // The selected location can be passed back via callback or navigation
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
        Log.d(TAG, "SearchViewModel cleared")
    }
}
