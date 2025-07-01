package com.example.infinite_track.presentation.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infinite_track.domain.use_case.auth.CheckSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Navigation states for the SplashScreen
sealed class SplashNavigationState {
    object Loading : SplashNavigationState()
    object NavigateToHome : SplashNavigationState()
    object NavigateToLogin : SplashNavigationState()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkSessionUseCase: CheckSessionUseCase
) : ViewModel() {

    // Private mutable state flow for navigation state
    private val _navigationState =
        MutableStateFlow<SplashNavigationState>(SplashNavigationState.Loading)

    // Public immutable state flow for navigation state
    val navigationState: StateFlow<SplashNavigationState> = _navigationState.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            checkSessionUseCase()
                .onSuccess {
                    // Session is valid, navigate to home
                    _navigationState.value = SplashNavigationState.NavigateToHome
                }
                .onFailure {
                    // Session is invalid or error occurred, navigate to login
                    _navigationState.value = SplashNavigationState.NavigateToLogin
                }
        }
    }
}
