package com.example.infinite_track.presentation.main

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.infinite_track.presentation.components.base.BaseLayout
import com.example.infinite_track.presentation.navigation.Screen
import com.example.infinite_track.presentation.navigation.appNavGraph

@Composable
fun InfiniteTrackApp(
    modifier: Modifier = Modifier
) {
    // Root level NavController - handles top-level navigation
    val navController = rememberNavController()

    // State animation defined at the app level - this will persist across screen navigations
    val infiniteTransition = rememberInfiniteTransition(label = "infinite_rotation")
    val rotationAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val sizeTransition = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        sizeTransition.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 10000
                    0f at 0
                    1f at 5000
                    0f at 10000
                },
                repeatMode = RepeatMode.Restart
            )
        )
    }

    // Main box that contains both the BaseLayout and the rest of the UI
    Box(modifier = modifier.fillMaxSize()) {
        // BaseLayout displayed at the back layer
        BaseLayout(
            rotation = rotationAnimation,
            size = sizeTransition.value,
            modifier = Modifier.zIndex(-2f)
        )

        // Semi-transparent white layer over BaseLayout
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(-1f)
                .background(Color.White.copy(alpha = 0.5f))
        )

        // Clean, minimal root container
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Transparent
        ) {
            // Root NavHost with only top-level navigation concerns
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route
            ) {
                // Connect to the app navigation graph
                appNavGraph(navController)
            }
        }
    }
}
