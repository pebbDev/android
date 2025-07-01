package com.example.infinite_track.presentation.components.maps

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.location

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun AttendanceMap(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var mapView: MapView? by remember { mutableStateOf(null) }

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    // Logika UI berdasarkan status izin
    when {
        // 1. Jika semua izin diberikan
        locationPermissionsState.allPermissionsGranted -> {
            LaunchedEffect(Unit) {
                // Ambil lokasi saat ini satu kali
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null && mapView != null) {
                        // Pusatkan kamera ke lokasi pengguna
                        mapView?.getMapboxMap()?.setCamera(
                            CameraOptions.Builder()
                                .center(
                                    com.mapbox.geojson.Point.fromLngLat(
                                        location.longitude,
                                        location.latitude
                                    )
                                )
                                .zoom(14.0)
                                .build()
                        )
                    }
                }
            }

            // Tampilkan peta Mapbox
            AndroidView(
                factory = {
                    MapView(it).apply {
                        mapView = this // Simpan referensi MapView
                        getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)
                        gestures.pitchEnabled = true
                        gestures.scrollEnabled = true
                        gestures.rotateEnabled = true
                        gestures.pinchToZoomEnabled = true
                        location.enabled = true
                    }
                },
                modifier = modifier.fillMaxSize()
            )
        }

        // 2. Jika izin ditolak (tapi masih bisa diminta lagi)
        locationPermissionsState.shouldShowRationale -> {
            PermissionRationale(
                text = "Aplikasi ini membutuhkan izin lokasi untuk menampilkan peta dan memvalidasi absensi Anda.",
                onRequestPermission = { locationPermissionsState.launchMultiplePermissionRequest() }
            )
        }

        // 3. Jika izin ditolak permanen atau kondisi awal
        else -> {
            PermissionRationale(
                text = "Izin lokasi diperlukan untuk fitur ini. Silakan aktifkan izin di pengaturan aplikasi.",
                onRequestPermission = { locationPermissionsState.launchMultiplePermissionRequest() }
            )
        }
    }
}

@Composable
private fun PermissionRationale(
    text: String,
    onRequestPermission: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = text,
                color = Color.White,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRequestPermission) {
                Text("Minta Izin")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AttendanceMapPreview() {
    // Preview akan menampilkan salah satu state dari permission rationale.
    // Menampilkan peta asli di preview bisa sangat berat.
    PermissionRationale(
        text = "Izin lokasi diperlukan untuk fitur ini.",
        onRequestPermission = {}
    )
}
