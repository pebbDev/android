package com.example.infinite_track.presentation.screen.attendance.search

import com.example.infinite_track.domain.model.location.LocationResult

/**
 * UI State untuk pencarian lokasi
 * Mendefisikan berbagai state yang mungkin terjadi selama pencarian
 */
sealed class SearchUiState {
    object Idle : SearchUiState()
    object Loading : SearchUiState()
    data class Success(val locations: List<LocationResult>) : SearchUiState()
    object Empty : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}
