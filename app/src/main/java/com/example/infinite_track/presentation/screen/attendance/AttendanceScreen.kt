package com.example.infinite_track.presentation.screen.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.infinite_track.presentation.components.button.attendance.AttendanceBottomSheetContent
import com.example.infinite_track.presentation.components.maps.AttendanceMap
import com.example.infinite_track.presentation.screen.attendance.components.AttendanceTopBar
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    navController: NavController,
    viewModel: AttendanceViewModel = hiltViewModel()
) {
    var mapViewInstance by remember { mutableStateOf<MapView?>(null) }

    // BottomSheet state
    val bottomSheetState = rememberStandardBottomSheetState(
        skipHiddenState = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    // Handle map events from ViewModel
    LaunchedEffect(Unit) {
        viewModel.mapEvent.collect { event ->
            when (event) {
                is AttendanceViewModel.MapEvent.AnimateToLocation -> {
                    mapViewInstance?.mapboxMap?.setCamera(
                        CameraOptions.Builder()
                            .center(Point.fromLngLat(event.longitude, event.latitude))
                            .zoom(16.0)
                            .build()
                    )
                }

                is AttendanceViewModel.MapEvent.ShowLocationError -> {
                    // Handle error
                }
            }
        }
    }

    // SOLUTION: Fullscreen map with proper touch event handling
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        containerColor = Color.Black.copy(alpha = 0.1f),
        contentColor = Color.Transparent,
        sheetContainerColor = Color.Transparent,
        sheetContentColor = Color.Unspecified,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetPeekHeight = 120.dp,
        sheetDragHandle = null,
        sheetContent = {
            // Enhanced Liquid Glass Background Container with better opacity
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.98f), // More opaque
                                Color.White.copy(alpha = 0.95f), // More opaque
                                Color(0xFFE3F2FD).copy(alpha = 0.92f), // More opaque
                                Color(0xFFBBDEFB).copy(alpha = 0.88f) // More opaque
                            )
                        )
                    )
            ) {
                // Enhanced glass effect overlays
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.6f), // More visible
                                    Color.Transparent,
                                    Color(0xFF81D4FA).copy(alpha = 0.3f) // More visible
                                ),
                                radius = 1000f
                            )
                        )
                        .blur(2.dp)
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White.copy(alpha = 0.4f), // More visible
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Content with drag handle
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Custom drag handle
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, bottom = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color.Gray.copy(alpha = 0.3f))
                        )
                    }

                    // Bottom sheet content
                    AttendanceBottomSheetContent(
                        modifier = Modifier.padding(top = 0.dp),
                        targetLocationTitle = "Kantor Pusat",
                        targetLocationAddress = "Jl. Sudirman No. 123, Jakarta Pusat, DKI Jakarta",
                        currentLocationTitle = "Lokasi Anda",
                        currentLocationAddress = "Mengambil lokasi saat ini...",
                        selectedWorkMode = "WFH",
                        isBookingEnabled = true,
                        isCheckInEnabled = true,
                        checkInButtonText = "Check In",
                        onModeSelected = { },
                        onBookingClick = { },
                        onCheckInClick = { }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Fullscreen Map that covers only status bar (keeping navigation bar visible)
            AttendanceMap(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets(0)) // Remove all system insets
                    .layout { measurable, constraints ->
                        // Less aggressive constraints - only cover status bar, keep navigation bar
                        val unlimitedConstraints = constraints.copy(
                            minWidth = 0,
                            minHeight = 0,
                            maxWidth = constraints.maxWidth * 2, // Reduced from 3x to 2x
                            maxHeight = constraints.maxHeight + 100  // Only extend to cover status bar
                        )

                        val placeable = measurable.measure(unlimitedConstraints)

                        // Position map to cover status bar only, keep navigation bar
                        layout(constraints.maxWidth, constraints.maxHeight) {
                            placeable.place(-50, -100) // Less aggressive - only cover status bar
                        }
                    },
                onMapReady = { mapView ->
                    mapViewInstance = mapView
                }
            )

            // TopBar overlay
            AttendanceTopBar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp),
                onBackClicked = {
                    navController.popBackStack()
                },
                onFocusLocationClicked = {
                    viewModel.onFocusLocationClicked()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AttendanceScreenPreview() {
    Infinite_TrackTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            AttendanceMap(
                modifier = Modifier.fillMaxSize(),
                onMapReady = { }
            )
            AttendanceTopBar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp),
                onBackClicked = { },
                onFocusLocationClicked = { }
            )
        }
    }
}
