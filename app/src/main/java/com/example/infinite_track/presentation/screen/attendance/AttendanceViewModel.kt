package com.example.infinite_track.presentation.screen.attendance

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infinite_track.domain.use_case.location.GetCurrentCoordinatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel untuk AttendanceScreen yang mengelola logika peta dan lokasi
 */
@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val getCurrentCoordinatesUseCase: GetCurrentCoordinatesUseCase
) : ViewModel() {

    /**
     * Sealed class untuk event yang dikirim ke UI terkait peta
     */
    sealed class MapEvent {
        data class AnimateToLocation(val latitude: Double, val longitude: Double) : MapEvent()
        object ShowLocationError : MapEvent()
    }

    // Channel untuk mengirim event satu kali dari ViewModel ke UI
    private val _mapEvent = Channel<MapEvent>()
    val mapEvent = _mapEvent.receiveAsFlow()

    companion object {
        private const val TAG = "AttendanceViewModel"
    }

    /**
     * Fungsi yang dipanggil saat tombol fokus lokasi ditekan
     * Menggunakan GetCurrentCoordinatesUseCase yang sudah ada dalam infrastruktur
     */
    fun onFocusLocationClicked() {
        viewModelScope.launch {
            try {
                getCurrentCoordinatesUseCase().onSuccess { coordinates ->
                    val (latitude, longitude) = coordinates
                    _mapEvent.send(
                        MapEvent.AnimateToLocation(
                            latitude = latitude,
                            longitude = longitude
                        )
                    )
                    Log.d(TAG, "Location found: $latitude, $longitude")
                }.onFailure { exception ->
                    _mapEvent.send(MapEvent.ShowLocationError)
                    Log.e(TAG, "Failed to get location", exception)
                }
            } catch (e: Exception) {
                _mapEvent.send(MapEvent.ShowLocationError)
                Log.e(TAG, "Unexpected error in onFocusLocationClicked", e)
            }
        }
    }
}
