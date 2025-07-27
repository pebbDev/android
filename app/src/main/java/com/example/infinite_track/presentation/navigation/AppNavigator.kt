package com.example.infinite_track.presentation.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Singleton navigator untuk menangani navigasi dari luar Composable context
 * Useful untuk navigasi dari Activity, BroadcastReceiver, atau Service
 */
@Singleton
class AppNavigator @Inject constructor() {

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    /**
     * Navigasi ke AttendanceScreen dari mana saja
     */
    fun navigateToAttendance() {
        _navigationEvents.tryEmit(NavigationEvent.NavigateToAttendance)
    }

    /**
     * Navigasi ke screen lain jika diperlukan
     */
    fun navigateToScreen(route: String) {
        _navigationEvents.tryEmit(NavigationEvent.NavigateToScreen(route))
    }

    /**
     * Navigasi ke login setelah sesi berakhir (401 error)
     */
    fun navigateToLoginAfterSessionExpired() {
        _navigationEvents.tryEmit(NavigationEvent.NavigateToLoginAfterSessionExpired)
    }
}

/**
 * Event untuk navigasi
 */
sealed class NavigationEvent {
    object NavigateToAttendance : NavigationEvent()
    data class NavigateToScreen(val route: String) : NavigationEvent()
    object NavigateToLoginAfterSessionExpired : NavigationEvent()
}
