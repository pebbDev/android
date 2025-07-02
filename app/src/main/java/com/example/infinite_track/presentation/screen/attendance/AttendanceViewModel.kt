package com.example.infinite_track.presentation.screen.attendance

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infinite_track.domain.model.attendance.Location
import com.example.infinite_track.domain.model.attendance.TargetLocationInfo
import com.example.infinite_track.domain.model.attendance.TodayStatus
import com.example.infinite_track.domain.use_case.attendance.GetTodayStatusUseCase
import com.example.infinite_track.domain.use_case.attendance.ValidateLocationUseCase
import com.example.infinite_track.domain.use_case.location.GetCurrentAddressUseCase
import com.example.infinite_track.domain.use_case.location.GetCurrentCoordinatesUseCase
import com.example.infinite_track.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Single state object that holds all data needed by AttendanceScreen UI
 */
data class AttendanceScreenState(
    val uiState: UiState<Unit> = UiState.Loading,
    val todayStatus: TodayStatus? = null,
    val targetLocation: Location? = null,
    val currentUserAddress: String = "",
    val currentUserLatitude: Double? = null,
    val currentUserLongitude: Double? = null,
    val isCheckInEnabled: Boolean = false,
    val isBookingEnabled: Boolean = false,
    val selectedWorkMode: String = "Work From Office",
    val distanceToTarget: Float? = null,
    val isWithinGeofence: Boolean = false,
    // New properties for map marker management
    val targetLocationMarker: Location? = null,
    val selectedMarkerInfo: Location? = null
) {
    // Convenience getter untuk UI components
    val targetLocationInfo: TargetLocationInfo?
        get() = targetLocation?.let { target ->
            TargetLocationInfo(
                description = target.description,
                locationName = target.category
            )
        }
}

/**
 * ViewModel untuk AttendanceScreen yang mengelola semua state dan logika absensi
 */

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val getTodayStatusUseCase: GetTodayStatusUseCase,
    private val getCurrentAddressUseCase: GetCurrentAddressUseCase,
    private val getCurrentCoordinatesUseCase: GetCurrentCoordinatesUseCase,
    private val validateLocationUseCase: ValidateLocationUseCase
) : ViewModel() {

    /**
     * Sealed class untuk event yang dikirim ke UI terkait peta
     */
    sealed class MapEvent {
        data class AnimateToLocation(val latitude: Double, val longitude: Double) : MapEvent()
        object ShowLocationError : MapEvent()
    }

    // Single StateFlow that holds all UI state
    private val _uiState = MutableStateFlow(AttendanceScreenState())
    val uiState: StateFlow<AttendanceScreenState> = _uiState.asStateFlow()

    // Channel untuk mengirim event satu kali dari ViewModel ke UI
    private val _mapEvent = Channel<MapEvent>()
    val mapEvent = _mapEvent.receiveAsFlow()

    // Job untuk tracking lokasi secara real-time
    private var locationTrackingJob: Job? = null

    companion object {
        private const val TAG = "AttendanceViewModel"
        private const val LOCATION_UPDATE_INTERVAL = 10000L // 10 seconds
    }

    init {
        initializeData()
    }

    /**
     * Fungsi privat untuk menginisialisasi semua data yang dibutuhkan
     */
    private fun initializeData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(uiState = UiState.Loading)

                // Ambil status hari ini terlebih dahulu
                fetchTodayStatus()

                // Mulai tracking lokasi setelah mendapat status
                startLocationUpdates()

            } catch (e: Exception) {
                Log.e(TAG, "Error initializing data", e)
                _uiState.value = _uiState.value.copy(
                    uiState = UiState.Error("Failed to initialize attendance data: ${e.message}")
                )
            }
        }
    }

    /**
     * Fungsi privat untuk mengambil status hari ini dari API
     */
    private suspend fun fetchTodayStatus() {
        try {
            getTodayStatusUseCase().onSuccess { todayStatus ->
                Log.d(TAG, "Today status fetched successfully: $todayStatus")

                val isBookingEnabled = todayStatus.activeMode.isNotEmpty()
                val selectedMode = todayStatus.activeMode.ifEmpty { "Work From Office" }

                _uiState.value = _uiState.value.copy(
                    todayStatus = todayStatus,
                    targetLocation = todayStatus.activeLocation,
                    targetLocationMarker = todayStatus.activeLocation, // Set marker for map display
                    isBookingEnabled = isBookingEnabled,
                    selectedWorkMode = selectedMode,
                    uiState = UiState.Success(Unit)
                )

                Log.d(
                    TAG,
                    "State updated with today status. Target location: ${todayStatus.activeLocation}"
                )

            }.onFailure { exception ->
                Log.e(TAG, "Failed to fetch today status", exception)
                _uiState.value = _uiState.value.copy(
                    uiState = UiState.Error("Failed to load attendance status: ${exception.message}")
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error in fetchTodayStatus", e)
            _uiState.value = _uiState.value.copy(
                uiState = UiState.Error("Unexpected error: ${e.message}")
            )
        }
    }

    /**
     * Fungsi privat untuk memulai tracking lokasi secara real-time
     */
    private fun startLocationUpdates() {
        // Cancel previous job if exists
        locationTrackingJob?.cancel()

        locationTrackingJob = viewModelScope.launch {
            while (true) {
                try {
                    updateCurrentLocation()
                    delay(LOCATION_UPDATE_INTERVAL)
                } catch (e: Exception) {
                    Log.e(TAG, "Error in location tracking loop", e)
                    delay(LOCATION_UPDATE_INTERVAL) // Continue tracking even if there's an error
                }
            }
        }
    }

    /**
     * Fungsi privat untuk mengupdate lokasi pengguna saat ini
     * Tidak lagi melakukan auto-focus ke lokasi user
     */
    private suspend fun updateCurrentLocation() {
        try {
            // Get current address
            getCurrentAddressUseCase().onSuccess { address ->
                _uiState.value = _uiState.value.copy(currentUserAddress = address)
                Log.d(TAG, "Current address updated: $address")
            }.onFailure { exception ->
                Log.w(TAG, "Failed to get current address", exception)
            }

            // Get current coordinates
            getCurrentCoordinatesUseCase().onSuccess { coordinates ->
                val (latitude, longitude) = coordinates
                _uiState.value = _uiState.value.copy(
                    currentUserLatitude = latitude,
                    currentUserLongitude = longitude
                )

                Log.d(TAG, "Current coordinates updated: $latitude, $longitude")

                // Validate geofence if target location is available
                validateGeofence(latitude, longitude)

            }.onFailure { exception ->
                Log.w(TAG, "Failed to get current coordinates", exception)
                // Reset check-in status if location can't be obtained
                _uiState.value = _uiState.value.copy(
                    isCheckInEnabled = false,
                    isWithinGeofence = false
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error in updateCurrentLocation", e)
        }
    }

    /**
     * Fungsi privat untuk memvalidasi apakah pengguna berada dalam geofence
     */
    private fun validateGeofence(userLatitude: Double, userLongitude: Double) {
        val targetLocation = _uiState.value.targetLocation
        val todayStatus = _uiState.value.todayStatus

        if (targetLocation != null && todayStatus != null) {
            val isWithinRadius = validateLocationUseCase(
                userLatitude = userLatitude,
                userLongitude = userLongitude,
                targetLatitude = targetLocation.latitude,
                targetLongitude = targetLocation.longitude,
                radius = targetLocation.radius
            )

            // Check-in enabled if within geofence AND can check in according to today status
            val canCheckIn = isWithinRadius && todayStatus.canCheckIn

            _uiState.value = _uiState.value.copy(
                isCheckInEnabled = canCheckIn,
                isWithinGeofence = isWithinRadius
            )

            Log.d(
                TAG,
                "Geofence validation: within radius = $isWithinRadius, can check in = $canCheckIn"
            )
        } else {
            Log.d(TAG, "Cannot validate geofence: missing target location or today status")
            _uiState.value = _uiState.value.copy(
                isCheckInEnabled = false,
                isWithinGeofence = false
            )
        }
    }

    /**
     * Fungsi yang dipanggil saat pengguna memilih mode kerja
     */
    fun onWorkModeSelected(mode: String) {
        Log.d(TAG, "Work mode selected: $mode")
        _uiState.value = _uiState.value.copy(selectedWorkMode = mode)

        // Re-validate geofence based on new mode if needed
        val currentLatitude = _uiState.value.currentUserLatitude
        val currentLongitude = _uiState.value.currentUserLongitude

        if (currentLatitude != null && currentLongitude != null) {
            validateGeofence(currentLatitude, currentLongitude)
        }
    }

    /**
     * Fungsi yang dipanggil saat tombol booking ditekan
     */
    fun onBookingClicked() {
        Log.d(TAG, "Booking clicked for mode: ${_uiState.value.selectedWorkMode}")
        // TODO: Implement booking logic
    }

    /**
     * Fungsi yang dipanggil saat tombol check-in ditekan
     */
    fun onCheckInClicked() {
        val currentState = _uiState.value
        Log.d(TAG, "Check-in clicked. Enabled: ${currentState.isCheckInEnabled}")

        if (currentState.isCheckInEnabled) {
            // TODO: Implement check-in logic
        }
    }

    /**
     * Fungsi yang dipanggil saat marker di peta diklik
     */
    fun onMarkerClicked(location: Location) {
        Log.d(TAG, "Marker clicked for location: ${location.description}")
        _uiState.value = _uiState.value.copy(selectedMarkerInfo = location)
    }

    /**
     * Fungsi yang dipanggil saat dialog marker info ditutup
     */
    fun onDismissMarkerInfo() {
        Log.d(TAG, "Marker info dialog dismissed")
        _uiState.value = _uiState.value.copy(selectedMarkerInfo = null)
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

                    // Update current user location in state untuk sinkronisasi
                    _uiState.value = _uiState.value.copy(
                        currentUserLatitude = latitude,
                        currentUserLongitude = longitude
                    )

                    // Send event untuk animasi kamera dengan zoom yang tepat
                    _mapEvent.send(
                        MapEvent.AnimateToLocation(
                            latitude = latitude,
                            longitude = longitude
                        )
                    )

                    Log.d(TAG, "Focus location - User position: $latitude, $longitude")
                }.onFailure { exception ->
                    _mapEvent.send(MapEvent.ShowLocationError)
                    Log.e(TAG, "Failed to get current location for focus", exception)
                }
            } catch (e: Exception) {
                _mapEvent.send(MapEvent.ShowLocationError)
                Log.e(TAG, "Unexpected error in onFocusLocationClicked", e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationTrackingJob?.cancel()
        Log.d(TAG, "ViewModel cleared, location tracking stopped")
    }
}
