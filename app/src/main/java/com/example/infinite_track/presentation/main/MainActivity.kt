package com.example.infinite_track.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.infinite_track.presentation.screen.splash.SplashNavigationState
import com.example.infinite_track.presentation.screen.splash.SplashViewModel
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // Get SplashViewModel instance
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen BEFORE super.onCreate()
        val splashScreen = installSplashScreen()

        // Set keep on screen condition - keep splash screen visible while in Loading state
        splashScreen.setKeepOnScreenCondition {
            viewModel.navigationState.value is SplashNavigationState.Loading
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Infinite_TrackTheme {
                InfiniteTrackApp()
            }
        }
    }
}