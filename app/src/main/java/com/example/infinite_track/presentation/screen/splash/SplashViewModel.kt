package com.example.infinite_track.presentation.screen.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infinite_track.domain.use_case.auth.CheckSessionUseCase
import com.example.infinite_track.domain.use_case.auth.LogoutUseCase
import com.example.infinite_track.utils.DialogHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val checkSessionUseCase: CheckSessionUseCase,
    private val logoutUseCase: LogoutUseCase,
    @ApplicationContext private val context: Context
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
                    // Session is valid and embedding generation (if needed) successful, navigate to home
                    _navigationState.value = SplashNavigationState.NavigateToHome
                }
                .onFailure { exception ->
                    // Session is invalid or embedding generation failed, show error dialog and logout
                    handleSessionFailure(exception)
                }
        }
    }

    private fun handleSessionFailure(exception: Throwable) {
        val errorMessage = when {
            exception.message?.contains("embedding", ignoreCase = true) == true ->
                "Gagal memverifikasi data wajah. Silakan login kembali untuk memperbarui profil Anda."

            exception.message?.contains("network", ignoreCase = true) == true ->
                "Koneksi internet bermasalah. Silakan periksa koneksi dan login kembali."

            exception.message?.contains("session", ignoreCase = true) == true ->
                "Sesi Anda telah berakhir. Silakan login kembali."

            else ->
                "Gagal memverifikasi data profil. Silakan login kembali."
        }

        // Show error dialog with appropriate message
        DialogHelper.showDialogError(
            context = context,
            title = "Verifikasi Gagal",
            textContent = errorMessage
        ) {
            // After user closes dialog, perform automatic logout
            performAutomaticLogout()
        }
    }

    private fun performAutomaticLogout() {
        viewModelScope.launch {
            try {
                // Clear all local data using LogoutUseCase
                logoutUseCase()
                    .onSuccess {
                        // Navigate to login after successful logout
                        _navigationState.value = SplashNavigationState.NavigateToLogin
                    }
                    .onFailure {
                        // Even if logout fails, still navigate to login for safety
                        _navigationState.value = SplashNavigationState.NavigateToLogin
                    }
            } catch (e: Exception) {
                // Fallback: navigate to login regardless of logout result
                _navigationState.value = SplashNavigationState.NavigateToLogin
            }
        }
    }
}