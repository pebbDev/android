package com.example.infinite_track.presentation.components.address

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.infinite_track.R
import com.example.infinite_track.presentation.theme.Purple_400
import com.example.infinite_track.presentation.theme.Purple_500
import com.example.infinite_track.presentation.theme.Violet_50
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressInputField(
    address: String,
    onAddressChange: (String) -> Unit
) {
    var selectedAddress by remember { mutableStateOf(address) }
    val textState = remember { mutableStateOf(TextFieldValue(selectedAddress)) }
    var openMapPicker by remember { mutableStateOf(false) }

    // Dialog for MapsScreen
    if (openMapPicker) {
        AlertDialog(
            onDismissRequest = { openMapPicker = false },
            text = {
                MapsScreen(
                    onPlaceSelected = { address ->
                        selectedAddress = address
                        textState.value = TextFieldValue(selectedAddress)
                        onAddressChange(selectedAddress)
                        openMapPicker = false
                    },
                    onMapClose = { openMapPicker = false }
                )
            },
            confirmButton = {}
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(
                        color = Violet_50.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .border(width = 1.dp, color = Violet_50, shape = RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_home),
                        contentDescription = "Home Icon",
                        tint = Purple_500
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    TextField(
                        value = textState.value,
                        onValueChange = {
                            textState.value = it
                            onAddressChange(it.text)
                        },
                        placeholder = {
                            Text(text = "Address", fontSize = 16.sp, color = Purple_400)
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .height(56.dp)
                    .width(56.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(color = Violet_50.copy(alpha = 0.5f), shape = RoundedCornerShape(15.dp))
                    .clickable {
                        openMapPicker = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_maps),
                    contentDescription = "Maps Icon",
                    tint = Purple_500,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun OpenPlacePicker(onPlaceSelected: (String, Double, Double) -> Unit) {
    val context = LocalContext.current
    val activity = context as Activity

    Places.initialize(context.applicationContext, "AIzaSyDoyWQjJqxgX5T-_ZX0Q3f553hOxaCEw1w")

    val locationPermission = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermission.value = true
        } else {
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val place = Autocomplete.getPlaceFromIntent(result.data!!)
            val address = place.address ?: "No Address"
            val latLng = place.latLng

            latLng?.let {
                onPlaceSelected(address, it.latitude, it.longitude)
            }
        }
    }

    if (locationPermission.value) {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(activity)
        launcher.launch(intent)
    }
}

@Preview(showBackground = true)
@Composable
fun AddressInputPreview() {
    AddressInputField(
        address = "",
        onAddressChange = {}
    )
}