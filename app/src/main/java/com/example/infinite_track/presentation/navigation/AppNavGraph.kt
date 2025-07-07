package com.example.infinite_track.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.infinite_track.presentation.main.MainScreen
import com.example.infinite_track.presentation.screen.attendance.AttendanceScreen
import com.example.infinite_track.presentation.screen.attendance.search.LocationSearchScreen
import com.example.infinite_track.presentation.screen.auth.LoginScreen
import com.example.infinite_track.presentation.screen.booking.DetailsMyBooking
import com.example.infinite_track.presentation.screen.splash.SplashScreen

fun NavGraphBuilder.appNavGraph(
    navController: NavHostController
) {
    // Splash Screen - entry point of the app
    composable(Screen.Splash.route) {
        SplashScreen(navController = navController)
    }

    // Auth graph - contains login screen
    navigation(
        startDestination = Screen.Login.route,
        route = "auth_graph"
    ) {
        // Login Screen
        composable(Screen.Login.route) {
            LoginScreen(
                navigateToHome = {
                    // Navigate to main_graph after successful login
                    navController.navigate(Screen.Home.route) {
                        // Clear backstack after successful login
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }

    // Main graph - contains all authenticated screens
    composable(Screen.Home.route) {
        MainScreen(rootNavController = navController)
    }

    // Attendance Screen
    composable(Screen.Attendance.route) {
        AttendanceScreen(navController = navController)
    }

    // Location Search Screen - untuk pencarian lokasi attendance
    composable(Screen.LocationSearch.route) {
        LocationSearchScreen(
            navController = navController,
            onLocationSelected = {
                // Handle location selection - bisa dikembangkan untuk menyimpan lokasi terpilih
                // Untuk sementara, kembali ke AttendanceScreen
                navController.navigateUp()
            }
        )
    }

    // Details My Booking Screen
    composable(Screen.DetailsMyBooking.route) {
        DetailsMyBooking(
            viewModel = hiltViewModel(),
            onBackClick = {
                navController.navigateUp()
            }
        )
    }
}
