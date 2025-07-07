package com.example.infinite_track.presentation.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.infinite_track.R
import com.example.infinite_track.presentation.components.calendar.Date
import com.example.infinite_track.presentation.components.profile_textfield.ProfileTextFieldComponent
import com.example.infinite_track.presentation.components.textfield.InfiniteTrackTextArea
import com.example.infinite_track.presentation.components.textfield.NumberTextFieldComponent
import com.example.infinite_track.presentation.core.headline4
import com.example.infinite_track.presentation.theme.Blue_500
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WfaBookingDialog(
    showDialog: Boolean,
    fullName: String,
    division: String,
    address: String,
    radius: String,
    description: String,
    schedule: String,
    notes: String,
    onRadiusChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onScheduleChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onSendClick: () -> Unit
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = Color.White.copy(alpha = 0.50f),
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                "WFA Booking Request",
                                style = headline4,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onDismissRequest) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close Dialog"
                                )
                            }
                        },
                        actions = {
                            TextButton(
                                onClick = onSendClick,
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Blue_500
                                )
                            ) {
                                Text("Kirim")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )
                }
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Informasi Karyawan
                    item {
                        Text(
                            text = "Informasi Karyawan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ProfileTextFieldComponent(
                            label = "Full Name",
                            value = fullName,
                            onValueChange = { /* Read-only field */ },
                            enabled = false // Tidak bisa diubah
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ProfileTextFieldComponent(
                            label = "Division",
                            value = division,
                            onValueChange = { /* Read-only field */ },
                            enabled = false // Tidak bisa diubah
                        )
                    }

                    // Informasi Lokasi
                    item {
                        Text(
                            text = "Informasi Lokasi",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ProfileTextFieldComponent(
                            label = "Address",
                            value = address,
                            onValueChange = { /* Read-only field */ },
                            enabled = false // Tidak bisa diubah
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        NumberTextFieldComponent(
                            label = "Radius (meters)",
                            value = radius,
                            onValueChange = onRadiusChange,
                            placeholder = "Enter radius in meters"
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        InfiniteTrackTextArea(
                            label = "Location Description",
                            value = description,
                            onValueChange = onDescriptionChange
                        )
                    }

                    // Jadwal
                    item {
                        Text(
                            text = "Jadwal",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Date(
                            label = "Schedule",
                            initialDate = schedule,
                            onDateSelected = onScheduleChange,
                            leadingIcon = painterResource(id = R.drawable.ic_calender)
                        )
                    }

                    // Catatan
                    item {
                        InfiniteTrackTextArea(
                            label = "Add Notes (Optional)",
                            value = notes,
                            onValueChange = onNotesChange
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Preview(backgroundColor = 0xFFFF1121, showBackground = true)
@Composable
fun WfaBookingDialogPreview() {
    Infinite_TrackTheme {
        // Contoh state management untuk preview
        var showDialog by remember { mutableStateOf(true) }
        var radius by remember { mutableStateOf("1000") }
        var description by remember { mutableStateOf("") }
        var schedule by remember { mutableStateOf("2025-06-15") }
        var notes by remember { mutableStateOf("") }

        // Tombol untuk menampilkan dialog (dalam aplikasi nyata)
        WfaBookingDialog(
            showDialog = showDialog,
            fullName = "Febriyadi",
            division = "Android Developer",
            address = "Jl. Sudirman No. 123, Jakarta Selatan",
            radius = radius,
            description = description,
            schedule = schedule,
            notes = notes,
            onRadiusChange = { radius = it },
            onDescriptionChange = { description = it },
            onScheduleChange = { schedule = it },
            onNotesChange = { notes = it },
            onDismissRequest = { showDialog = false },
            onSendClick = {
                // Logika untuk mengirim data
                println("Data dikirim: Radius=$radius, Description=$description, Jadwal=$schedule, Catatan=$notes")
                showDialog = false
            }
        )
    }
}
