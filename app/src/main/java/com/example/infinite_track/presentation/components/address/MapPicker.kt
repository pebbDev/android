package com.example.infinite_track.presentation.components.address

import android.Manifest
import android.content.Context
import android.location.Geocoder
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapPicker(onLocationSelected: (String) -> Unit) {  
    val context = LocalContext.current
    val geocoder = remember { Geocoder(context, Locale.getDefault()) }
    val coroutineScope = rememberCoroutineScope()

    val defaultLocation = LatLng(-6.1751, 106.8650)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 10f)
    }

    val locationPermissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
    val selectedLocation = remember { mutableStateOf<LatLng?>(null) }

    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }

    if (locationPermissionState.status.isGranted) {
        val mapStyle = getMapStyle(context)
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                myLocationButtonEnabled = true,
                compassEnabled = true
            ),
            onMapClick = { latLng ->
                selectedLocation.value = latLng


                coroutineScope.launch {
                    val address = getAddressFromLatLng(geocoder, latLng)
                    onLocationSelected(address)
                }
            }
        ) {
            selectedLocation.value?.let { latLng ->
                Marker(
                    state = MarkerState(position = latLng),
                    title = "Marker Title"
                )
            }
            Marker(
                state = MarkerState(position = defaultLocation),
                title = "Default Location"
            )
        }
    } else {
        Text("Izin lokasi belum diberikan. Silakan berikan izin untuk menggunakan peta.")
    }
}

private suspend fun getAddressFromLatLng(geocoder: Geocoder, latLng: LatLng): String {
    return withContext(Dispatchers.IO) {
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (addresses?.isNotEmpty() == true) {
            addresses[0].getAddressLine(0) ?: "Alamat tidak ditemukan"
        } else {
            "Alamat tidak ditemukan"
        }
    }
}

private fun getMapStyle(context: Context): MapStyleOptions? {
    return null
}

