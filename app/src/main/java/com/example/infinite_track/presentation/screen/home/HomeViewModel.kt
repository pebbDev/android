package com.example.infinite_track.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infinite_track.domain.model.attendance.AttendanceRecord
import com.example.infinite_track.domain.model.auth.UserModel
import com.example.infinite_track.domain.model.dashboard.InternshipSummary
import com.example.infinite_track.domain.use_case.auth.GetLoggedInUserUseCase
import com.example.infinite_track.domain.use_case.dashboard.GetInternshipDashboardDataUseCase
import com.example.infinite_track.domain.use_case.history.GetAttendanceHistoryUseCase
import com.example.infinite_track.domain.use_case.location.GetCurrentAddressUseCase
import com.example.infinite_track.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val getAttendanceHistoryUseCase: GetAttendanceHistoryUseCase,
    private val getCurrentAddressUseCase: GetCurrentAddressUseCase,
    private val getInternshipDashboardDataUseCase: GetInternshipDashboardDataUseCase
) : ViewModel() {

    // User profile state
    private val _userProfileState = MutableStateFlow<UserModel?>(null)
    val userProfileState: StateFlow<UserModel?> = _userProfileState

    // Top attendance history state for Employee/Manager
    private val _topAttendanceHistoryState =
        MutableStateFlow<UiState<List<AttendanceRecord>>>(UiState.Loading)
    val topAttendanceHistoryState: StateFlow<UiState<List<AttendanceRecord>>> =
        _topAttendanceHistoryState

    // Location state
    private val _currentAddressState = MutableStateFlow("Loading...")
    val currentAddressState: StateFlow<String> = _currentAddressState

    // Internship Dashboard Summary state - simplified to just hold the data or null
    private val _internshipSummaryState = MutableStateFlow<InternshipSummary?>(null)
    val internshipSummaryState: StateFlow<InternshipSummary?> = _internshipSummaryState

    // Dummy leave data for Employee/Manager
    private val _annualBalance = MutableStateFlow(12)
    val annualBalance: StateFlow<Int> = _annualBalance

    private val _annualUsed = MutableStateFlow(5)
    val annualUsed: StateFlow<Int> = _annualUsed

    init {
        fetchUserProfile()
        fetchTopAttendanceHistory()
        fetchCurrentAddress()
        loadDummyLeaveData()
        fetchInternshipDashboardData()
    }

    private fun fetchUserProfile() {
        viewModelScope.launch {
            getLoggedInUserUseCase().collect { user ->
                _userProfileState.value = user
            }
        }
    }

    private fun fetchTopAttendanceHistory() {
        viewModelScope.launch {
            _topAttendanceHistoryState.value = UiState.Loading

            getAttendanceHistoryUseCase(
                period = "monthly",
                page = 1,
                limit = 5
            ).onSuccess { historyPage ->
                _topAttendanceHistoryState.value = UiState.Success(historyPage.records)
            }.onFailure { error ->
                _topAttendanceHistoryState.value = UiState.Error(error.message ?: "Unknown error occurred")
            }
        }
    }

    private fun fetchCurrentAddress() {
        viewModelScope.launch {
            getCurrentAddressUseCase().onSuccess { address ->
                _currentAddressState.value = address
            }.onFailure {
                _currentAddressState.value = "Unable to fetch location"
            }
        }
    }

    private fun loadDummyLeaveData() {
        // These values are already set in the StateFlow initialization
        // This function is included for clarity and future expansion
        _annualBalance.value = 12
        _annualUsed.value = 5
    }

    private fun fetchInternshipDashboardData() {
        viewModelScope.launch {
            getInternshipDashboardDataUseCase().onSuccess { summary ->
                _internshipSummaryState.value = summary
            }.onFailure {
                // On failure, just leave the state as null
                _internshipSummaryState.value = null
            }
        }
    }

    /**
     * Function to refresh the internship dashboard data
     * Can be called when user performs a pull-to-refresh or after check-in/check-out
     */
    fun refreshInternshipDashboard() {
        fetchInternshipDashboardData()
    }

    /**
     * Function to refresh the attendance history data
     * Can be called when user performs a pull-to-refresh
     */
    fun refreshAttendanceHistory() {
        fetchTopAttendanceHistory()
    }
}
