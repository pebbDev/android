package com.example.infinite_track.presentation.components.maps

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.infinite_track.domain.model.attendance.Location
import com.example.infinite_track.utils.MapUtils
import com.example.infinite_track.utils.PermissionUtils
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.location

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun AttendanceMap(
    modifier: Modifier = Modifier,
    targetLocation: Location? = null,
    currentUserLocation: Point? = null,
    onMarkerClick: (Location) -> Unit = {},
    onMapReady: ((MapView) -> Unit)? = null
) {
    var mapView: MapView? by remember { mutableStateOf(null) }

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    // Effect to update markers when targetLocation changes
    LaunchedEffect(targetLocation, mapView) {
        if (targetLocation != null && mapView != null) {
            // Add a small delay to ensure map is fully initialized
            kotlinx.coroutines.delay(500)

            val mapboxMap = mapView!!.mapboxMap

            // Wait for map style to be loaded and add markers
            mapboxMap.getStyle { style ->
                MapUtils.addMarkersAndRadius(mapView!!, targetLocation, onMarkerClick)
            }
        }
    }

    // Effect to handle initial camera positioning - Focus on Target Location by default
    LaunchedEffect(targetLocation, mapView) {
        if (mapView != null && targetLocation != null) {
            val mapboxMap = mapView!!.mapboxMap

            // Wait for map style to be fully loaded
            mapboxMap.getStyle { style ->
                // Default focus ke target location untuk attendance checking
                val centerPoint =
                    Point.fromLngLat(targetLocation.longitude, targetLocation.latitude)

                // Set initial camera position dengan fokus ke target location
                mapboxMap.flyTo(
                    CameraOptions.Builder()
                        .center(centerPoint)
                        .zoom(16.0) // Optimal zoom untuk melihat area sekitar target
                        .pitch(0.0) // Ensure top-down view for accuracy
                        .bearing(0.0) // North-facing orientation
                        .build(),
                    MapAnimationOptions.Builder()
                        .duration(1000L)
                        .build()
                )

                android.util.Log.d(
                    "AttendanceMap",
                    "Default camera positioned at target location: ${centerPoint.latitude()}, ${centerPoint.longitude()}"
                )
            }
        }
    }

    when {
        locationPermissionsState.allPermissionsGranted -> {
            // Tampilkan peta Mapbox
            AndroidView(
                factory = {
                    MapView(it).apply {
                        mapView = this // Simpan referensi MapView
                        mapboxMap.loadStyle(Style.MAPBOX_STREETS)
                        gestures.pitchEnabled = true
                        gestures.scrollEnabled = true
                        gestures.rotateEnabled = true
                        gestures.pinchToZoomEnabled = true
                        location.enabled = true

                        // Panggil callback onMapReady jika tersedia
                        onMapReady?.invoke(this)
                    }
                },
                modifier = modifier.fillMaxSize()
            )
        }

        locationPermissionsState.shouldShowRationale -> {
            PermissionUtils.PermissionRationale(
                text = "Aplikasi ini membutuhkan izin lokasi untuk menampilkan peta dan memvalidasi absensi Anda.",
                onRequestPermission = { locationPermissionsState.launchMultiplePermissionRequest() }
            )
        }

        else -> {
            PermissionUtils.PermissionRationale(
                text = "Izin lokasi diperlukan untuk fitur ini. Silakan aktifkan izin di pengaturan aplikasi.",
                onRequestPermission = { locationPermissionsState.launchMultiplePermissionRequest() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AttendanceMapPreview() {
    AttendanceMap()
}