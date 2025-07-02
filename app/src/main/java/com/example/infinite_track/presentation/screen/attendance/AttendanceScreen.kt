package com.example.infinite_track.presentation.screen.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.infinite_track.domain.model.attendance.TargetLocationInfo
import com.example.infinite_track.presentation.components.button.attendance.AttendanceBottomSheetContent
import com.example.infinite_track.presentation.components.empty.ErrorAnimation
import com.example.infinite_track.presentation.components.loading.LoadingAnimation
import com.example.infinite_track.presentation.components.maps.AttendanceMap
import com.example.infinite_track.presentation.components.maps.MarkerView
import com.example.infinite_track.presentation.screen.attendance.components.AttendanceTopBar
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme
import com.example.infinite_track.utils.UiState
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    navController: NavController,
    viewModel: AttendanceViewModel = hiltViewModel()
) {
    var mapViewInstance by remember { mutableStateOf<MapView?>(null) }

    // Observasi state tunggal dari ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // BottomSheet state
    val bottomSheetState = rememberStandardBottomSheetState(
        skipHiddenState = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    // Handle map events from ViewModel dengan animasi yang lebih smooth
    LaunchedEffect(Unit) {
        viewModel.mapEvent.collect { event ->
            when (event) {
                is AttendanceViewModel.MapEvent.AnimateToLocation -> {
                    mapViewInstance?.let { mapView ->
                        // Gunakan flyTo untuk animasi yang lebih smooth ke lokasi terkini
                        val cameraOptions = CameraOptions.Builder()
                            .center(Point.fromLngLat(event.longitude, event.latitude))
                            .zoom(18.0) // Zoom lebih dekat untuk fokus yang akurat
                            .pitch(0.0) // Pastikan top-down view
                            .bearing(0.0) // North-facing orientation
                            .build()

                        mapView.mapboxMap.flyTo(
                            cameraOptions,
                            MapAnimationOptions.Builder()
                                .duration(1200L) // Sedikit lebih cepat untuk responsivitas
                                .build()
                        )
                    }
                }

                is AttendanceViewModel.MapEvent.ShowLocationError -> {
                    // Handle error - bisa ditambahkan snackbar atau toast
                    android.util.Log.e(
                        "AttendanceScreen",
                        "Failed to get current location for focus"
                    )
                }
            }
        }
    }

    // Penanganan state utama berdasarkan UiState dengan smart cast fix
    when (val currentUiState = uiState.uiState) {
        is UiState.Idle -> {
            // Initial idle state - could show a splash or continue to loading
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingAnimation()
            }
        }

        is UiState.Loading -> {
            // Tampilkan loading animation
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingAnimation()
            }
        }

        is UiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ErrorAnimation()
                    Text(
                        text = currentUiState.errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        is UiState.Success -> {
            // Tampilkan konten utama dengan BottomSheet
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
                    // Enhanced Liquid Glass Background Container
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.98f),
                                        Color.White.copy(alpha = 0.95f),
                                        Color(0xFFE3F2FD).copy(alpha = 0.92f),
                                        Color(0xFFBBDEFB).copy(alpha = 0.88f)
                                    )
                                )
                            )
                    ) {
                        // Glass effect overlays
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.6f),
                                            Color.Transparent,
                                            Color(0xFF81D4FA).copy(alpha = 0.3f)
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
                                            Color.White.copy(alpha = 0.4f),
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

                            AttendanceBottomSheetContent(
                                modifier = Modifier.padding(top = 0.dp),
                                targetLocationInfo = uiState.targetLocation?.let { target ->
                                    TargetLocationInfo(
                                        description = target.description,
                                        locationName = target.category
                                    )
                                },
                                currentLocationAddress = uiState.currentUserAddress.ifEmpty { "Mengambil lokasi saat ini..." },
                                selectedWorkMode = uiState.selectedWorkMode,
                                isBookingEnabled = uiState.isBookingEnabled,
                                isCheckInEnabled = uiState.isCheckInEnabled,
                                checkInButtonText = if (uiState.isWithinGeofence) "Check In" else "Di Luar Jangkauan",
                                onModeSelected = { mode -> viewModel.onWorkModeSelected(mode) },
                                onBookingClick = { viewModel.onBookingClicked() },
                                onCheckInClick = { viewModel.onCheckInClicked() }
                            )
                        }
                    }
                }
            ) { paddingValues ->
                Box(modifier = Modifier.fillMaxSize()) {
                    // Fullscreen Map dengan data dari ViewModel - Fixed layout
                    AttendanceMap(
                        modifier = Modifier.fillMaxSize(),
                        targetLocation = uiState.targetLocationMarker,
                        currentUserLocation = uiState.currentUserLatitude?.let { lat ->
                            uiState.currentUserLongitude?.let { lng ->
                                Point.fromLngLat(lng, lat)
                            }
                        },
                        onMarkerClick = { location -> viewModel.onMarkerClicked(location) },
                        onMapReady = { mapView ->
                            mapViewInstance = mapView
                        }
                    )

                    // Top bar with location focus button - fixed parameters
                    AttendanceTopBar(
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(16.dp),
                        onBackClicked = { navController.navigateUp() },
                        onFocusLocationClicked = { viewModel.onFocusLocationClicked() }
                    )

                    uiState.selectedMarkerInfo?.let { selectedMarker ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 80.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            MarkerView(
                                title = selectedMarker.description,
                                description = "Kategori: ${selectedMarker.category}",
                                radius = "${selectedMarker.radius} meter",
                                coordinates = "${selectedMarker.latitude}, ${selectedMarker.longitude}",
                                onClose = { viewModel.onDismissMarkerInfo() }
                            )
                        }
                    }
                }
            }
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
