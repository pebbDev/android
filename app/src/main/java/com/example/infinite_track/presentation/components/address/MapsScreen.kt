package com.example.infinite_track.presentation.components.address

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapsScreen(
    onPlaceSelected: (String) -> Unit,
    onMapClose: () -> Unit
) {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-6.1751, 106.8650), 10f)
    }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var selectedAddress by remember { mutableStateOf("") }
    var selectedLatLng by remember { mutableStateOf<LatLng?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val locationPermission = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermission.value = true
        }
    }

    fun searchLocation(query: String, context: Context) {
        if (query.isEmpty()) return

        coroutineScope.launch {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addressList = withContext(Dispatchers.IO) {
                geocoder.getFromLocationName(query, 1)
            }
            val location = addressList?.firstOrNull()?.let {
                LatLng(it.latitude, it.longitude)
            }
            location?.let {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
                selectedLatLng = it
                selectedAddress = addressList.first().getAddressLine(0) ?: "Unknown Address"
            }
        }
    }

    fun fetchAddressFromLatLng(latLng: LatLng, context: Context, onAddressFetched: (String) -> Unit) {
        coroutineScope.launch {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addressList = withContext(Dispatchers.IO) {
                geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            }
            val address = addressList?.firstOrNull()?.getAddressLine(0) ?: "Unknown Address"
            onAddressFetched(address)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (locationPermission.value) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = true),
                onMapClick = { latLng ->
                    selectedLatLng = latLng
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
                    fetchAddressFromLatLng(latLng, context) { address ->
                        selectedAddress = address
                    }
                }
            ) {
                selectedLatLng?.let {
                    Marker(
                        state = MarkerState(position = it),
                        title = "Selected Location",
                        snippet = selectedAddress
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    searchLocation(it.text, context)
                },
                placeholder = { Text(text = "Search location", fontSize = 16.sp) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Blue,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth().background(Color.White)
            )
        }


        Button(
            onClick = {
                onPlaceSelected(selectedAddress)
                onMapClose()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .width(200.dp)
        ) {
            Text("Select Location")
        }
    }
}
