package com.example.infinite_track.presentation.screen.attendance

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infinite_track.data.soucre.local.preferences.AttendancePreference
import com.example.infinite_track.domain.model.attendance.Location
import com.example.infinite_track.domain.model.attendance.TargetLocationInfo
import com.example.infinite_track.domain.model.attendance.TodayStatus
import com.example.infinite_track.domain.use_case.attendance.GetTodayStatusUseCase
import com.example.infinite_track.domain.use_case.location.GetCurrentAddressUseCase
import com.example.infinite_track.domain.use_case.location.GetCurrentCoordinatesUseCase
import com.example.infinite_track.presentation.geofencing.GeofenceManager
import com.example.infinite_track.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Simplified state object focused on reactive geofence integration
 * Removed manual distance calculation and GPS tracking logic
 */
data class AttendanceScreenState(
    val uiState: UiState<Unit> = UiState.Loading,
    val todayStatus: TodayStatus? = null,
    val targetLocation: Location? = null,
    val currentUserAddress: String = "",
    val currentUserLatitude: Double? = null,
    val currentUserLongitude: Double? = null,
    val isBookingEnabled: Boolean = false,
    val selectedWorkMode: String = "Work From Office",
    // Map-specific properties
    val targetLocationMarker: Location? = null,
    val selectedMarkerInfo: Location? = null
) {
    // Convenience getter for UI components
    val targetLocationInfo: TargetLocationInfo?
        get() = targetLocation?.let { target ->
            TargetLocationInfo(
                description = target.description,
                locationName = target.category
            )
        }
}

/**
 * Simplified ViewModel that is fully reactive to geofence state
 * Removed manual GPS tracking and distance calculation logic
 * Uses geofence as the single source of truth for validation
 */
@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val getTodayStatusUseCase: GetTodayStatusUseCase,
    private val getCurrentAddressUseCase: GetCurrentAddressUseCase,
    private val getCurrentCoordinatesUseCase: GetCurrentCoordinatesUseCase,
    private val attendancePreference: AttendancePreference,
    private val geofenceManager: GeofenceManager
) : ViewModel() {

    /**
     * Sealed class for map events sent to UI
     */
    sealed class MapEvent {
        data class AnimateToLocation(val latitude: Double, val longitude: Double) : MapEvent()
        object ShowLocationError : MapEvent()
    }

    // Main UI state
    private val _uiState = MutableStateFlow(AttendanceScreenState())
    val uiState: StateFlow<AttendanceScreenState> = _uiState.asStateFlow()

    // Reactive geofence status from DataStore (single source of truth for validation)
    val isUserInsideGeofence: StateFlow<Boolean> = attendancePreference
        .isUserInsideGeofence()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    // Reactive check-in eligibility combining today status and geofence status
    val isCheckInEnabled: StateFlow<Boolean> = combine(
        _uiState,
        isUserInsideGeofence
    ) { state, isInside ->
        state.todayStatus?.canCheckIn == true && isInside
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    // Channel for one-time events to UI
    private val _mapEvent = Channel<MapEvent>()
    val mapEvent = _mapEvent.receiveAsFlow()

    // Job for UI-focused location updates (display purposes only)
    private var displayLocationJob: Job? = null

    companion object {
        private const val TAG = "AttendanceViewModel"
        private const val DISPLAY_UPDATE_INTERVAL = 10000L // 10 seconds for UI updates
    }

    init {
        initializeData()
    }

    /**
     * Initialize data by fetching today status and starting display updates
     */
    private fun initializeData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(uiState = UiState.Loading)

                // Fetch today status first
                fetchTodayStatus()

                // Start location updates for display purposes only
                startDisplayLocationUpdates()

            } catch (e: Exception) {
                Log.e(TAG, "Error initializing data", e)
                _uiState.value = _uiState.value.copy(
                    uiState = UiState.Error("Failed to initialize attendance data: ${e.message}")
                )
            }
        }
    }

    /**
     * Fetch today status and setup geofence if target location exists
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
                    targetLocationMarker = todayStatus.activeLocation,
                    isBookingEnabled = isBookingEnabled,
                    selectedWorkMode = selectedMode,
                    uiState = UiState.Success(Unit)
                )

                // Setup geofence for target location (validation purposes)
                todayStatus.activeLocation?.let { location ->
                    setupGeofence(location)
                }

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
     * Setup geofence for validation (background monitoring)
     */
    private fun setupGeofence(location: Location) {
        try {
            geofenceManager.addGeofence(
                id = location.locationId.toString(),
                latitude = location.latitude,
                longitude = location.longitude,
                radius = location.radius.toFloat()
            )
            Log.d(TAG, "Geofence setup for location: ${location.description}")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to setup geofence", e)
        }
    }

    /**
     * Start location updates for display purposes only (UI updates)
     * This runs only while ViewModel is active for better UX
     */
    private fun startDisplayLocationUpdates() {
        displayLocationJob?.cancel()

        displayLocationJob = viewModelScope.launch {
            while (true) {
                try {
                    updateDisplayLocation()
                    delay(DISPLAY_UPDATE_INTERVAL)
                } catch (e: Exception) {
                    Log.e(TAG, "Error in display location updates", e)
                    delay(DISPLAY_UPDATE_INTERVAL)
                }
            }
        }
    }

    /**
     * Update location data for display purposes only
     * Does not affect validation logic
     */
    private suspend fun updateDisplayLocation() {
        try {
            // Update current address for display
            getCurrentAddressUseCase().onSuccess { address ->
                _uiState.value = _uiState.value.copy(currentUserAddress = address)
                Log.d(TAG, "Display address updated: $address")
            }.onFailure { exception ->
                Log.w(TAG, "Failed to get display address", exception)
            }

            // Update current coordinates for map display
            getCurrentCoordinatesUseCase().onSuccess { coordinates ->
                val (latitude, longitude) = coordinates
                _uiState.value = _uiState.value.copy(
                    currentUserLatitude = latitude,
                    currentUserLongitude = longitude
                )
                Log.d(TAG, "Display coordinates updated: $latitude, $longitude")
            }.onFailure { exception ->
                Log.w(TAG, "Failed to get display coordinates", exception)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error in updateDisplayLocation", e)
        }
    }

    /**
     * Handle work mode selection
     */
    fun onWorkModeSelected(mode: String) {
        Log.d(TAG, "Work mode selected: $mode")
        _uiState.value = _uiState.value.copy(selectedWorkMode = mode)
    }

    /**
     * Handle booking button click
     */
    fun onBookingClicked() {
        Log.d(TAG, "Booking clicked for mode: ${_uiState.value.selectedWorkMode}")
        // TODO: Implement booking logic
    }

    /**
     * Handle check-in button click
     */
    fun onCheckInClicked() {
        viewModelScope.launch {
            val canCheckIn = isCheckInEnabled.value
            Log.d(TAG, "Check-in clicked. Enabled: $canCheckIn")

            if (canCheckIn) {
                // TODO: Implement check-in logic
                Log.d(TAG, "Proceeding with check-in...")
            } else {
                Log.d(TAG, "Check-in not allowed - user outside geofence or can't check in")
            }
        }
    }

    /**
     * Handle map marker click
     */
    fun onMarkerClicked(location: Location) {
        Log.d(TAG, "Marker clicked for location: ${location.description}")
        _uiState.value = _uiState.value.copy(selectedMarkerInfo = location)
    }

    /**
     * Handle marker info dialog dismissal
     */
    fun onDismissMarkerInfo() {
        Log.d(TAG, "Marker info dialog dismissed")
        _uiState.value = _uiState.value.copy(selectedMarkerInfo = null)
    }

    /**
     * Handle focus location button click
     */
    fun onFocusLocationClicked() {
        viewModelScope.launch {
            try {
                getCurrentCoordinatesUseCase().onSuccess { coordinates ->
                    val (latitude, longitude) = coordinates

                    // Update state for immediate display
                    _uiState.value = _uiState.value.copy(
                        currentUserLatitude = latitude,
                        currentUserLongitude = longitude
                    )

                    // Send map animation event
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
        displayLocationJob?.cancel()

        // Clean up geofence when ViewModel is cleared
        _uiState.value.targetLocation?.let { location ->
            geofenceManager.removeGeofence(location.locationId.toString())
        }

        Log.d(TAG, "ViewModel cleared, display location tracking stopped")
    }
}
