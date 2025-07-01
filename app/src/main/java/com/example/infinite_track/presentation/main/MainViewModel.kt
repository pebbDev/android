package com.example.infinite_track.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infinite_track.domain.use_case.auth.GetLoggedInUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for the main app container
 * Handles global UI state like user role
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    getLoggedInUserUseCase: GetLoggedInUserUseCase
) : ViewModel() {

    /**
     * StateFlow exposing the current user role
     * Defaults to "Guest" if no user is logged in
     */
    val userRole: StateFlow<String> = getLoggedInUserUseCase()
        .map { user ->
            user?.roleName ?: "Guest"
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "Guest"
        )
}
